package com.example.demo.service;

import com.example.demo.entity.Article;
import com.example.demo.entity.Client;
import com.example.demo.entity.Facture;
import com.example.demo.entity.LigneFacture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Service
@Transactional
public class InitData {

    @Autowired
    private EntityManager em;

    public void insertTestData() {

    	// Client 1 : 2 facture 2 ligne de facture 2 articles avec des noms aux caractères spéciaux
    	// Pour satisfaire tous mes test manuels
        Client client  = new Client();
        client.setNom("P;ETRILLO");
        client.setPrenom("Alexandre");
        em.persist(client);

        // ARTICLES FACTURE 1
        Article article = new Article();
        article.setLibelle("Carte mère ASROCK 2345");
        article.setPrix(79.90);
        em.persist(article);
        
        Article article2 = new Article();
        article2.setLibelle("Des Fleurs");
        article2.setPrix(35.5);
        em.persist(article2);
        
        // ARTICLES FACTURE 2
        Article article3 = new Article();
        article3.setLibelle("Du SWAG");
        article3.setPrix(79.90);
        em.persist(article3);
        
        Article article4 = new Article();
        article4.setLibelle("Des chocolat");
        article4.setPrix(35.5);
        em.persist(article4);

        // FACTURES
        Facture facture = new Facture();
        facture.setClient(client);
        em.persist(facture);
        
        Facture facture2 = new Facture();
        facture2.setClient(client);
        em.persist(facture2);

        //LIGNES DE FACTURES FACTURE 1
        LigneFacture ligneFacture1 = new LigneFacture();
        ligneFacture1.setFacture(facture);
        ligneFacture1.setArticle(article);
        ligneFacture1.setQuantite(1);
        em.persist(ligneFacture1);
        
        LigneFacture ligneFacture2 = new LigneFacture();
        ligneFacture2.setFacture(facture);
        ligneFacture2.setArticle(article2);
        ligneFacture2.setQuantite(100);
        em.persist(ligneFacture2);
        
        //LIGNES DE FACTURES FACTURE 2
        LigneFacture ligneFacture3 = new LigneFacture();
        ligneFacture3.setFacture(facture2);
        ligneFacture3.setArticle(article3);
        ligneFacture3.setQuantite(1);
        em.persist(ligneFacture3);
        
        LigneFacture ligneFacture4 = new LigneFacture();
        ligneFacture4.setFacture(facture2);
        ligneFacture4.setArticle(article4);
        ligneFacture4.setQuantite(100);
        em.persist(ligneFacture4);
        
        // Client 2, n'a aucune facture, juste la pour tester
        // la recherche de tous les clients
        Client client2  = new Client();
        client2.setNom("Be;y");
        client2.setPrenom("Clement");
        em.persist(client2);
    }
}
