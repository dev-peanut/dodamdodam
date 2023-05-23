package com.app.dodamdodam.service.board.recruitmentBoard;

import com.app.dodamdodam.domain.RecruitmentBoardDTO;
import com.app.dodamdodam.domain.RecruitmentBoardFileDTO;
import com.app.dodamdodam.domain.RecruitmentFileDTO;
import com.app.dodamdodam.domain.RecruitmentMemberDTO;
import com.app.dodamdodam.entity.recruitment.RecruitmentBoard;
import com.app.dodamdodam.entity.recruitment.RecruitmentFile;
import com.app.dodamdodam.repository.board.recruitment.RecruitmentBoardRepository;
import com.app.dodamdodam.repository.board.recruitment.RecruitmentFileRepository;
import com.app.dodamdodam.repository.member.MemberRepository;
import com.app.dodamdodam.search.board.AdminRecruitmentSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecruitmentBoardServiceImpl implements RecruitmentBoardService {
    @Autowired
    private RecruitmentBoardRepository recruitmentBoardRepository;
    private final MemberRepository memberRepository;
    private final RecruitmentFileRepository recruitmentFileRepository;

    // 이벤트 게시판 등록
    @Override
    public void register(RecruitmentBoardDTO recruitmentBoardDTO, Long memberId) {
        List<RecruitmentFileDTO> recruitmentFileDTOS = recruitmentBoardDTO.getRecruitmentFileDTOS();

        memberRepository.findById(memberId).ifPresent(
                member -> recruitmentBoardDTO.setMemberDTO(toMemberDTO(member))
        );

        RecruitmentBoard recruitmentBoard = recruitmentBoardRepository.save(toRecruitmentBoardEntity(recruitmentBoardDTO));

        if(recruitmentFileDTOS != null){
            for (int i = 0; i < recruitmentFileDTOS.size(); i++) {
                RecruitmentFileDTO recruitmentFileDTO = recruitmentFileDTOS.get(i);
                RecruitmentFile recruitmentFile = toRecruitmentFileEntity(recruitmentFileDTO);
                recruitmentFile.setRecruitmentBoard(recruitmentBoard);

                recruitmentFileRepository.save(recruitmentFile);
            }
        }
    }

//    내가 작성한 모집 게시글 목록
    @Override
    public List<RecruitmentBoardFileDTO> getRecruimentBoardListByMemberId(Pageable pageable, Long memberId) {
        return recruitmentBoardRepository.findRecruitmentBoardListByMemberId_QueryDSL(pageable,memberId)
                .stream().map(recruitmentBoard -> toRecruitmentBoardFileDto(recruitmentBoard)).collect(Collectors.toList());
    }

//    내가 작성한 모집 게시글에 참가한 인원들
    @Override
    public RecruitmentMemberDTO getRecruitmentedMembersByBoardId(Long boardId) {
        return toRecruitmentMemberDTO(recruitmentBoardRepository.findRecruitmentedMembersByBoardId_QueryDSL(boardId));
    }


    //    내가 참가한 모집 게시글 목록
    @Override
    public List<RecruitmentBoardFileDTO> getRecruimentedBoardListByMemberId(Pageable pageable, Long memberId) {
        return recruitmentBoardRepository.findRecruitmentedBoardListByMemberId_QueryDSL(pageable,memberId)
                .stream().map(recruitmentBoard -> toRecruitmentBoardFileDto(recruitmentBoard)).collect(Collectors.toList());
    }

    //  관리자 목록
    @Override
    public Page<RecruitmentBoardFileDTO> showList(Pageable pageable) {
            Page<RecruitmentBoard> recruitmentBoardPage = recruitmentBoardRepository.findAllWithPaging(pageable);
            List<RecruitmentBoardFileDTO> recruitmentFileDTOS = recruitmentBoardPage.get().map(this::toRecruitmentBoardFileDto).collect(Collectors.toList());
            return new PageImpl<>(recruitmentFileDTOS, pageable, recruitmentBoardPage.getTotalElements());
        }

     //    관리자 검색
    @Override
    public Page<RecruitmentBoardFileDTO> showAdminRecruitmentWithSearch_QueryDSL(Pageable pageable, AdminRecruitmentSearch adminRecruitmentSearch) {
            Page<RecruitmentBoard> recruitmentBoardPage = recruitmentBoardRepository.findAdminRecruitmentBoardWithPaging_QueryDSL(adminRecruitmentSearch, pageable);
            List<RecruitmentBoardFileDTO> recruitmentFileDTOS = recruitmentBoardPage.getContent().stream()
                    .map(this::toRecruitmentBoardFileDto)
                    .collect(Collectors.toList());
            return new PageImpl<>(recruitmentFileDTOS, pageable, recruitmentBoardPage.getTotalElements());
        }

    //  관리자 삭제
    @Override
    public void deleteRecruitmentBoard(List<Long> recruitmentBoardIds) {
        for(Long recruitmentBoardId: recruitmentBoardIds){
            recruitmentBoardRepository.deleteById(recruitmentBoardId);
        }
    }

    //관리자 상세
    @Override
    public RecruitmentBoardFileDTO getAdminRecruitmentBoardDetail(Long id) {
        Optional<RecruitmentBoard> recruitmentBoard = recruitmentBoardRepository.findById(id);
        return toRecruitmentBoardFileDto(recruitmentBoard.get());
    }


    // 모집 게시글 목록
    @Override
    public List<RecruitmentBoardFileDTO> getRecruitmentBoardListByPaging(Pageable pageable) {
        return recruitmentBoardRepository.findRecruitmentBoardList_QueryDSL(pageable).stream().map(recruitmentBoard -> toRecruitmentBoardFileDto(recruitmentBoard)).collect(Collectors.toList());
    }

    /* 모집 게시판 최근 게시글 5개 */
    @Override
    public List<RecruitmentBoardFileDTO> getRecentRecruitmentBoardList() {
        List<RecruitmentBoardFileDTO> recruitmentBoardFileDTOS = recruitmentBoardRepository.findRecentRecruitmentBoardList_QueryDSL().stream().map(recentRecruitmentBoardList -> toRecruitmentBoardFileDto(recentRecruitmentBoardList)).collect(Collectors.toList());
        return recruitmentBoardFileDTOS;
    }

}
