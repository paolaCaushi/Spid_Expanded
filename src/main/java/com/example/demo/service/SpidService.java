package com.example.demo.service;

import com.example.demo.model.Spid;
import com.example.demo.model.User;
import com.example.demo.repository.SpidRepository;
import com.example.demo.utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class SpidService {

    @Autowired
    private SpidRepository spidRepository;


    public List<Spid> getSpidByUserId(User user) {
        return spidRepository.findSpidByUserId(user);
    }


    @Transactional
    public Spid createSpid(Spid spid) throws Exception {
        List<Spid> spids = spidRepository.findSpidByUserId(spid.getUserId());
        if (spids.size() > 0) {
            throw new Exception("This user has an existing SPID");
        }
        spid.setCreatedBy(spid.getUserId().getUsername());
        return spidRepository.save(spid);
    }

    //get all spids
    public Iterable<Spid> retrieveAllSpids() {
        return spidRepository.findAll();
    }

    //change status
    public Spid changeStatus(Spid spid) {
        spid.setStatus(Status.READY_FOR_REVIEW);
        return spidRepository.save(spid);
    }

    public Spid getSpid(User user) throws Exception {
        List<Spid> spid = spidRepository.findSpidByUserId(user);
        if (spid.size() <= 0) {
            throw new Exception("This user has no SPID");
        }
        return spid.get(0);
    }

    public void deleteSpid(Spid spid) throws Exception {
        if (spid.getStatus() != Status.PENDING) {
            throw new Exception("You cannot delete this SPID");
        }
        spidRepository.delete(spid);
    }
}