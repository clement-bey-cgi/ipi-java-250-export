package com.example.demo.dto;


/**
 * Created by Kayne on 09/04/2018.
 */
public class ClientDTO {
	
	private Long id;

    private String prenom;

    private String nom;
   
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
