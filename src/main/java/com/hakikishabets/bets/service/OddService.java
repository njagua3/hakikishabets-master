package com.hakikishabets.bets.service;

import com.hakikishabets.bets.dto.OddRequestDTO;
import com.hakikishabets.bets.exception.ResourceNotFoundException;
import com.hakikishabets.bets.model.Market;
import com.hakikishabets.bets.model.Odd;
import com.hakikishabets.bets.repository.MarketRepository;
import com.hakikishabets.bets.repository.OddRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OddService {

    private final OddRepository oddRepository;

    @Autowired
    public OddService(OddRepository oddRepository) {
        this.oddRepository = oddRepository;
    }

    public Odd getOddById(Long id) {
        return oddRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Odd not found with ID: " + id));
    }

    @Autowired
    private MarketRepository marketRepository;

    public Odd addOddToMarket(Long marketId, OddRequestDTO requestDTO) {
        Market market = marketRepository.findById(marketId)
                .orElseThrow(() -> new ResourceNotFoundException("Market not found with ID: " + marketId));

        Odd odd = new Odd();
        odd.setSelection(requestDTO.getSelection());
        odd.setValue(requestDTO.getValue());
        odd.setMarket(market);

        return oddRepository.save(odd);
    }

}
