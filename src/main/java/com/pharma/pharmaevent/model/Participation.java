package com.pharma.pharmaevent.model;

import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Participation {
	
	public Participation(Delegate delegate, Event event) {
	    this.delegate = delegate;
	    this.event = event;
	}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Delegate delegate;

    @ManyToOne
    private Event event;

    private boolean checked = false;
}
