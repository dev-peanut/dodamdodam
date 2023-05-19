package com.app.dodamdodam.service.board.eventBoard;

import com.app.dodamdodam.domain.EventBoardDTO;
import com.app.dodamdodam.domain.EventFileDTO;
import com.app.dodamdodam.entity.event.EventBoard;
import com.app.dodamdodam.entity.event.EventFile;
import com.app.dodamdodam.entity.member.Member;
import com.app.dodamdodam.exception.BoardNotFoundException;
import com.app.dodamdodam.exception.MemberNotFoundException;
import com.app.dodamdodam.repository.board.event.board.EventBoardRepository;
import com.app.dodamdodam.repository.board.event.file.EventFileRepository;
import com.app.dodamdodam.repository.member.MemberRepository;
import com.app.dodamdodam.type.FileRepresent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Qualifier("eventBoard")
@Slf4j
@Transactional
public class EventBoardServiceImpl implements EventBoardService {
    private final EventBoardRepository eventBoardRepository;
    private final MemberRepository memberRepository;
    private final EventFileRepository eventFileRepository;

    @Override
    public EventBoardDTO getDetail(Long id) {
        EventBoard eventBoard = eventBoardRepository.findEventBoardById_QueryDSL(id).orElseThrow(() -> {
            throw new BoardNotFoundException();
        });
        return eventBoardToDTO(eventBoard);
    }
// 저장하기
@Override
public void write(EventBoardDTO eventBoardDTO, Long memberId) {
    List<EventFileDTO> fileDTOS = eventBoardDTO.getEventFiles();

    memberRepository.findById(memberId).ifPresent(
            member -> eventBoardDTO.setMemberDTO(toMemberDTO(member))
    );
    log.info("들어옴@@@@@@@@@@@@@@@@@@");
    log.info(eventBoardDTO.toString());
    log.info(toEventBoardEntity(eventBoardDTO).toString());

    eventBoardRepository.save(toEventBoardEntity(eventBoardDTO));
    if(fileDTOS != null){
        for (int i = 0; i < fileDTOS.size(); i++) {
            if(i == 0){
                fileDTOS.get(i).setFileRepresent(FileRepresent.REPRESENT);
            }else {
                fileDTOS.get(i).setFileRepresent(FileRepresent.ORDINARY);
            }
            fileDTOS.get(i).setEventBoard(getCurrentSequence());
            eventFileRepository.save(toEventFileEntity(fileDTOS.get(i)));
        }
    }
}
//    현재 시퀀스 가져오기
    @Override
    public EventBoard getCurrentSequence() {
        return eventBoardRepository.getCurrentSequence_QueryDsl();
    }


    @Override
    public Slice<EventBoardDTO> getEventBoards(Pageable pageable) {
        Slice<EventBoard> eventBoards =
                eventBoardRepository.findAllByIdDescWithPaging_QueryDSL(PageRequest.of(0, 10));
        List<EventBoardDTO> collect = eventBoards.get().map(board -> eventBoardToDTO(board)).collect(Collectors.toList());

        return new SliceImpl<>(collect, pageable, eventBoards.hasNext());
    }
}