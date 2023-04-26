package com.owl.api.example.service;

import com.owl.api.example.dto.MotherboardResponseDTO;
import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class MotherboardService {

    private OntologyManager ontologyManager;

    private OWLDataProperty hasName;
    private OWLDataProperty hasDimensions;
    private OWLDataProperty hasFormFactor;
    private OWLObjectProperty hasChipset;
    private OWLObjectProperty hasSocket;
    private OWLClassExpression classMotherboard;
    private OWLOntologyManager manager;
    private OWLDataFactory dataFactory;
    private OWLReasonerFactory reasonerFactory;
    private OWLReasoner reasoner;


    public MotherboardService() throws OWLOntologyCreationException, OWLOntologyStorageException {
        ontologyManager = new OntologyManager();
        manager = OWLManager.createOWLOntologyManager();
        dataFactory = manager.getOWLDataFactory();
        reasonerFactory = new ReasonerFactory();
        reasoner = reasonerFactory.createReasoner(this.ontologyManager.getOntology());
        hasName = dataFactory.getOWLDataProperty(IRI.create("http://www.semanticweb.org/administrator/ontologies/2023/2/untitled-ontology-3#motherboard_has_name"));
        hasDimensions = dataFactory.getOWLDataProperty(IRI.create("http://www.semanticweb.org/administrator/ontologies/2023/2/untitled-ontology-3#motherboard_has_dimensions"));
        hasFormFactor = dataFactory.getOWLDataProperty(IRI.create("http://www.semanticweb.org/administrator/ontologies/2023/2/untitled-ontology-3#motherboard_has_form_factor"));
        hasSocket = dataFactory.getOWLObjectProperty(IRI.create("http://www.semanticweb.org/administrator/ontologies/2023/2/untitled-ontology-3#motherboard_has_socket"));
        hasChipset = dataFactory.getOWLObjectProperty(IRI.create("http://www.semanticweb.org/administrator/ontologies/2023/2/untitled-ontology-3#has_chipset"));
        classMotherboard = dataFactory.getOWLClass(IRI.create("http://www.semanticweb.org/administrator/ontologies/2023/2/untitled-ontology-3#Motherboard"));
    }

    public List<MotherboardResponseDTO> getAllMotherboards(){
        return getMotherboardResponseDTOs(classMotherboard);
    }

    private List<MotherboardResponseDTO> getMotherboardResponseDTOs(OWLClassExpression classMotherboard) {
        Set<OWLNamedIndividual> individuals = reasoner.getInstances(classMotherboard, false).getFlattened();
        List<MotherboardResponseDTO> motherboards = new ArrayList<>();
        for (OWLNamedIndividual individual : individuals) {
            motherboards.add(setSpec(individual));
        }
        return motherboards;
    }

    private MotherboardResponseDTO setSpec(OWLNamedIndividual individual) {
        MotherboardResponseDTO motherboardResponseDTO = new MotherboardResponseDTO();
        for (OWLDataPropertyAssertionAxiom assertion : ontologyManager.getOntology().getDataPropertyAssertionAxioms(individual)) {
            OWLDataProperty property = assertion.getProperty().asOWLDataProperty();
            if (hasName.equals(property)) {
                OWLLiteral value = assertion.getObject();
                motherboardResponseDTO.setName(value.getLiteral());
            } else if (hasDimensions.equals(property)) {
                OWLLiteral value = assertion.getObject();
                motherboardResponseDTO.setDimensions(value.getLiteral());
            } else if (hasFormFactor.equals(property)) {
                OWLLiteral value = assertion.getObject();
                motherboardResponseDTO.setFormFactor(value.getLiteral());
            }
        }
        Set<OWLNamedIndividual> sockets = reasoner.getObjectPropertyValues(individual, hasSocket).getFlattened();
        OWLNamedIndividual socket = sockets.stream().findFirst().orElse(null);
        motherboardResponseDTO.setSocket(socket.getIRI().getShortForm());
        Set<OWLNamedIndividual> chipsets = reasoner.getObjectPropertyValues(individual, hasChipset).getFlattened();
        OWLNamedIndividual chipset = chipsets.stream().findFirst().orElse(null);
        motherboardResponseDTO.setChipset(chipset.getIRI().getShortForm());
        return motherboardResponseDTO;
    }
}
