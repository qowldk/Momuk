package com.uplus.eureka.domain.participant.service;

import com.uplus.eureka.domain.participant.Participant;
import com.uplus.eureka.domain.participant.repository.ParticipantMapper;
import com.uplus.eureka.domain.vote.repository.VoteMapper;
import lombok.RequiredArgsConstructor;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantMapper participantMapper;
    private final VoteMapper voteMapper;

    @Transactional
    public void registerParticipation(Integer voteId, String userId) {
        // 투표 존재 여부 확인
        var vote = voteMapper.getVoteById(voteId);
        if (vote == null) {
            throw new RuntimeException("존재하지 않는 투표입니다.");
        }

        // 투표 가능 상태인지 확인
        if (!"active".equals(vote.getStatus())) {
            throw new RuntimeException("이미 마감된 투표입니다.");
        }

        // 이미 참여했는지 확인 (중복 참여 방지)
        if (participantMapper.existsByUserIdAndVoteId(userId, voteId)) {
            throw new RuntimeException("이미 참여한 투표입니다.");
        }

        // 참여 등록
        Participant participant = new Participant();
        participant.setVoteId(voteId);
        participant.setUserId(userId);
        participant.setCreatedAt(LocalDateTime.now());
        participantMapper.insertParticipant(participant);

        // 참여 인원 업데이트
        voteMapper.incrementParticipants(voteId);
    }


    @Transactional
    public void cancelParticipation(Integer voteId, String userId) {
        // 투표 존재 여부 확인
        if (voteMapper.getVoteById(voteId) == null) {
            throw new RuntimeException("존재하지 않는 투표입니다.");
        }

        // 참여 여부 확인 (취소하려는 투표에 참여한 사용자만 처리)
        if (!participantMapper.existsByUserIdAndVoteId(userId, voteId)) {
            throw new RuntimeException("참여하지 않은 투표입니다.");
        }

        // 참여 취소
        participantMapper.deleteParticipant(voteId, userId);

        // 참여 인원 감소
        voteMapper.decrementParticipant(voteId);
    }
    @Transactional(readOnly = true)
    public List<Participant> getParticipants(Integer voteId) {
        // 투표 존재 여부 확인
        var vote = voteMapper.getVoteById(voteId);
        if (vote == null) {
            throw new RuntimeException("존재하지 않는 투표입니다.");
        }

        // 참여자 리스트 조회
//        List<Participant> participants = participantMapper.findParticipantsByVoteId(voteId);
//        System.out.println("!!!!!!!!" + participants.size());
        return participantMapper.findParticipantsByVoteId(voteId);
    }

}
