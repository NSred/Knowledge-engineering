package com.owl.api.example.service;

import com.owl.api.example.additionalConfiguration.GlobalStrings;
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

    private OWLObjectProperty enablesCPU;
    private OWLObjectProperty enablesGPU;
    private OWLObjectProperty enablesRAM;
    private OWLObjectProperty enablesPSU;
    private OWLClassExpression classMotherboard;
    private OWLOntologyManager manager;
    private OWLDataFactory dataFactory;
    private OWLReasonerFactory reasonerFactory;
    private OWLReasoner reasoner;


    public MotherboardService(OntologyManager ontologyManager) {
        this.ontologyManager = ontologyManager;;
        manager = OWLManager.createOWLOntologyManager();
        dataFactory = manager.getOWLDataFactory();
        reasonerFactory = new ReasonerFactory();
        reasoner = reasonerFactory.createReasoner(this.ontologyManager.getOntology());
        hasName = dataFactory.getOWLDataProperty(IRI.create(GlobalStrings.baseIRI + "motherboard_has_name"));
        hasDimensions = dataFactory.getOWLDataProperty(IRI.create(GlobalStrings.baseIRI + "motherboard_has_dimensions"));
        hasFormFactor = dataFactory.getOWLDataProperty(IRI.create(GlobalStrings.baseIRI + "motherboard_has_form_factor"));
        hasSocket = dataFactory.getOWLObjectProperty(IRI.create(GlobalStrings.baseIRI + "motherboard_has_socket"));
        hasChipset = dataFactory.getOWLObjectProperty(IRI.create(GlobalStrings.baseIRI + "has_chipset"));
        enablesCPU = dataFactory.getOWLObjectProperty(IRI.create(GlobalStrings.baseIRI + "enables_attachment_cpu"));
        enablesGPU = dataFactory.getOWLObjectProperty(IRI.create(GlobalStrings.baseIRI + "enables_attachment_graphic_card"));
        enablesRAM = dataFactory.getOWLObjectProperty(IRI.create(GlobalStrings.baseIRI + "enables_attachment_ram"));
        enablesPSU = dataFactory.getOWLObjectProperty(IRI.create(GlobalStrings.baseIRI + "enables_attachment_psu"));
        classMotherboard = dataFactory.getOWLClass(IRI.create(GlobalStrings.baseIRI + "Motherboard"));
    }

    public List<MotherboardResponseDTO> getAllMotherboards(){
        return getMotherboardResponseDTOs(classMotherboard);
    }

    public List<MotherboardResponseDTO> getMotherboardUpgrades(String motherboard, String cpu, String gpu, String ram, String psu){

        OWLNamedIndividual motherboardIndividual = dataFactory.getOWLNamedIndividual(IRI.create(GlobalStrings.baseIRI + motherboard.replace(" ", "_")));

        Set<OWLNamedIndividual> chipsetIndividuals = reasoner.getObjectPropertyValues(motherboardIndividual, hasChipset).getFlattened();
        OWLNamedIndividual chipsetIndividual = chipsetIndividuals.stream().findFirst().orElse(null);

        Set<OWLLiteral> formFactorLiterals = reasoner.getDataPropertyValues(motherboardIndividual, hasFormFactor);
        OWLLiteral formFactorLiteral = formFactorLiterals.stream().findFirst().orElse(null);

        OWLNamedIndividual cpuIndividual = dataFactory.getOWLNamedIndividual(IRI.create(GlobalStrings.baseIRI + cpu.replace(" ", "_")));
        OWLNamedIndividual gpuIndividual = dataFactory.getOWLNamedIndividual(IRI.create(GlobalStrings.baseIRI + gpu.replace(" ", "_")));
        OWLNamedIndividual ramIndividual = dataFactory.getOWLNamedIndividual(IRI.create(GlobalStrings.baseIRI + ram.replace(" ", "_")));
        OWLNamedIndividual psuIndividual = dataFactory.getOWLNamedIndividual(IRI.create(GlobalStrings.baseIRI + psu.replace(" ", "_")));


        OWLClassExpression queryExpression = dataFactory.getOWLObjectIntersectionOf(
                classMotherboard,
                dataFactory.getOWLObjectHasValue(hasChipset, chipsetIndividual),
                dataFactory.getOWLDataHasValue(hasFormFactor, formFactorLiteral),
                dataFactory.getOWLObjectHasValue(enablesRAM, ramIndividual),
                dataFactory.getOWLObjectHasValue(enablesCPU, cpuIndividual),
                dataFactory.getOWLObjectHasValue(enablesGPU, gpuIndividual),
                dataFactory.getOWLObjectHasValue(enablesPSU, psuIndividual)
                );
        return getMotherboardResponseDTOs(queryExpression);
    }

    private List<MotherboardResponseDTO> getMotherboardResponseDTOs(OWLClassExpression classMotherboard) {
        Set<OWLNamedIndividual> individuals = reasoner.getInstances(classMotherboard, false).getFlattened();
        System.out.println("\nTotal number of retrieved individuals: " + individuals.size());
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
        motherboardResponseDTO.setSocket(socket.getIRI().getShortForm().replace("_", " "));
        Set<OWLNamedIndividual> chipsets = reasoner.getObjectPropertyValues(individual, hasChipset).getFlattened();
        OWLNamedIndividual chipset = chipsets.stream().findFirst().orElse(null);
        motherboardResponseDTO.setChipset(chipset.getIRI().getShortForm().replace("_", " "));
        return motherboardResponseDTO;
    }
}
