package com.example.demo.service;

import com.example.demo.dto.FactureDTO;
import com.example.demo.dto.LigneFactureDTO;
import com.example.demo.entity.Facture;
import com.example.demo.entity.LigneFacture;
import com.example.demo.repository.FactureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;

@Service
@Transactional
public class FactureService {

    @Autowired
    private FactureRepository factureRepository;
    @Autowired
    private ClientMapper clientMapper;
    
    @Autowired
    private ClientService clientService;

    public List<FactureDTO> findAllFactures() {
        return factureRepository.findAll().stream().map(this::toDTO).collect(toList());
    }
    
    public List<FactureDTO> findByClientId(Long id) {
    	List<Facture> factures = factureRepository.findByClientId(id);
    	FactureDTO dto;
    	List<FactureDTO> result = new ArrayList<>();
    	List<LigneFactureDTO> lignes;
    	for (Facture facture : factures) {
    		dto = new FactureDTO();
    		dto.setClient(clientService.toDto(facture.getClient()));
    		
    		// Récupération 
    		lignes = new ArrayList<>();
    		LigneFactureDTO currentLigne;
    		for (LigneFacture ligne : facture.getLigneFactures()) {
    			lignes.add(mapLigneFacture(ligne));
    		}
    		
    		dto.setLigneFactures(lignes);
    		dto.setId(facture.getId());
    		result.add(dto);
    	}
    	return result;
    }

    private FactureDTO toDTO(Facture f) {
        FactureDTO factureDTO = new FactureDTO();
        factureDTO.setId(f.getId());
        factureDTO.setClient(clientMapper.map(f.getClient()));
        factureDTO.setLigneFactures(f.getLigneFactures().stream().map(this::mapLigneFacture).collect(toList()));
        return factureDTO;
    }

    private LigneFactureDTO mapLigneFacture(LigneFacture lf) {
        LigneFactureDTO ligneFactureDTO = new LigneFactureDTO();
        ligneFactureDTO.setDesignation(lf.getArticle().getLibelle());
        ligneFactureDTO.setQuantite(lf.getQuantite());
        ligneFactureDTO.setPrixUnitaire(lf.getArticle().getPrix());
        return ligneFactureDTO;
    }

    public FactureDTO findById(Long id) {
        return factureRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() ->
                new IllegalArgumentException("Facture inconnu " + id)
        );
    }
}
