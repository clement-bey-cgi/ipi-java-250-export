package com.example.demo.service;

import com.example.demo.dto.ClientDTO;
import com.example.demo.entity.Client;
import com.example.demo.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientMapper clientMapper;

    public List<ClientDTO> findAllClients() {
        return clientRepository.findAll().stream().map(c-> clientMapper.map(c)).collect(toList());
    }
    
    public ClientDTO toDto(Client c) {
    	ClientDTO dto = new ClientDTO();
    	dto.setNom(c.getNom());
    	dto.setPrenom(c.getPrenom());
    	return dto;
    }

    /**Recupère un client à partir de son id technique. Renvoi un DTO correspondant.
     * @param id l'id technique du client
     * @return le DTO équivalent à l'entité*/
	public ClientDTO findOne(Long id) {
		return clientMapper.map(clientRepository.getOne(id));
	}
}
