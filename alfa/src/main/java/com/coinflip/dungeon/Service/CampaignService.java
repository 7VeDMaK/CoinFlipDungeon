package com.coinflip.dungeon.Service;

import com.coinflip.dungeon.Repository.CampaignRepository;
import com.coinflip.dungeon.Domain.Campaign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampaignService {

    @Autowired
    CampaignRepository campaignRepository;

    public List<Campaign> sayHelloAndPrintCampaigns(){
        List<Campaign> campaignList = campaignRepository.findAll();
        System.out.println("Hello world!");
        return campaignList;
    }
}
