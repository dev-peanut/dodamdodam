package com.app.dodamdodam.service.board.purchase;

import com.app.dodamdodam.domain.PurchaseBoardDTO;
import com.app.dodamdodam.entity.purchase.PurchaseBoard;
import com.app.dodamdodam.repository.board.purchase.PurchaseBoardRepository;
import com.app.dodamdodam.search.PurchaseBoardSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Qualifier("purchaseBoard") @Primary
@RequiredArgsConstructor
public class PurchaseBoardServiceImpl implements PurchaseBoardService {
    private final PurchaseBoardRepository purchaseBoardRepository;

    @Override
    public List<PurchaseBoardDTO> getPurchaseBoardsWithSearch(PurchaseBoardSearch purchaseBoardSearch, Pageable pageable) {
        List<PurchaseBoardDTO> purchaseBoardDTOS = purchaseBoardRepository.findAllWithSearch_QueryDSL(purchaseBoardSearch, pageable)
                .stream().map(purchaseBoard -> toPurchaseBoardDTO(purchaseBoard)).collect(Collectors.toList());

        return purchaseBoardDTOS;
    }
}