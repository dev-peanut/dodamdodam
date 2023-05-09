package com.app.dodamdodam.repository;

import com.app.dodamdodam.entity.chatting.Chatting;
import com.app.dodamdodam.entity.chatting.Room;
import com.app.dodamdodam.entity.free.FreeBoard;
import com.app.dodamdodam.repository.chatting.ChattingRepository;
import com.app.dodamdodam.repository.member.MemberRepository;
import com.app.dodamdodam.repository.room.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback(false)
@Slf4j
public class ChattingRepositoryTests {
    @Autowired
    private ChattingRepository chattingRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Test
    public void saveTest(){
        Room room = new Room(1L);
        memberRepository.findById(1L).ifPresent(member -> room.setMember(member));
        Chatting chatting = new Chatting(1L, 2L, "1번이 2번에게 보내는 메세지");
        Chatting chatting2 = new Chatting(1L, 2L, "1번이 2번에게 보내는 메세지2");
        chatting.setRoom(room);
        chatting2.setRoom(room);
        chattingRepository.save(chatting);
        chattingRepository.save(chatting2);
        roomRepository.save(room);
    }

//    @Test
//    public void findAll(){
//        chattingRepository.findAll().stream().map(Chatting::toString).forEach(log::info);
//    }



}

