package friutrodez.backendtourneecommercial.dto;

import friutrodez.backendtourneecommercial.model.EtapesParcours;

import java.util.List;

public record ParcoursDTO(List<EtapesParcours> etapes, String nom) {}