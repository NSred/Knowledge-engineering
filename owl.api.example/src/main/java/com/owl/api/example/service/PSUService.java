package com.owl.api.example.service;

import com.owl.api.example.additionalConfiguration.GlobalStrings;
import com.owl.api.example.dto.PSUResponseDTO;
import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.vocab.OWLFacet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class PSUService {

    private OntologyManager ontologyManager;

    private OWLDataProperty hasName;
    private OWLDataProperty hasCertificate;
    private OWLDataProperty hasPower;
    private OWLDataProperty hasDimensions;
    private OWLObjectProperty attachedPSU;
    private OWLObjectProperty supplyGPU;
    private OWLClassExpression classPSU;
    private OWLOntologyManager manager;
    private OWLDataFactory dataFactory;
    private OWLReasonerFactory reasonerFactory;
    private OWLReasoner reasoner;


    public PSUService(OntologyManager ontologyManager) {
        this.ontologyManager = ontologyManager;;
        manager = OWLManager.createOWLOntologyManager();
        dataFactory = manager.getOWLDataFactory();
        reasonerFactory = new ReasonerFactory();
        reasoner = reasonerFactory.createReasoner(this.ontologyManager.getOntology());
        hasName = dataFactory.getOWLDataProperty(IRI.create(GlobalStrings.baseIRI + "psu_has_name"));
        hasPower = dataFactory.getOWLDataProperty(IRI.create(GlobalStrings.baseIRI + "psu_has_power_in_watts"));
        hasCertificate = dataFactory.getOWLDataProperty(IRI.create(GlobalStrings.baseIRI + "psu_has_certificate"));
        hasDimensions = dataFactory.getOWLDataProperty(IRI.create(GlobalStrings.baseIRI + "psu_has_dimensions"));
        attachedPSU = dataFactory.getOWLObjectProperty(IRI.create(GlobalStrings.baseIRI + "psu_attached_to"));
        supplyGPU = dataFactory.getOWLObjectProperty(IRI.create(GlobalStrings.baseIRI + "psu_supplies_gpu"));
        classPSU = dataFactory.getOWLClass(IRI.create(GlobalStrings.baseIRI + "PSU"));
    }

    public List<PSUResponseDTO> getAllPSUs(){
        return getPSUResponseDTOs(classPSU);
    }

    public PSUResponseDTO getPSUByName(String name){
        List<PSUResponseDTO> psuResponseDTOS = getAllPSUs();
        for(PSUResponseDTO psuDTO : psuResponseDTOS){
            if(psuDTO.getName().equals(name))
                return psuDTO;
        }
        return null;
    }

    public List<PSUResponseDTO> getPSUUpgrades(String psu, String motherboard, String gpu){

        OWLNamedIndividual psuIndividual = dataFactory.getOWLNamedIndividual(IRI.create(GlobalStrings.baseIRI + psu.replace(" ", "_")));

        Set<OWLLiteral> powerLiterals = reasoner.getDataPropertyValues(psuIndividual, hasPower);
        OWLLiteral powerLiteral = powerLiterals.stream().findFirst().orElse(null);

        OWLDataRange powerRange = dataFactory.getOWLDatatypeRestriction(dataFactory.getIntegerOWLDatatype(),
                dataFactory.getOWLFacetRestriction(OWLFacet.MIN_EXCLUSIVE, dataFactory.getOWLLiteral(Integer.parseInt(powerLiteral.getLiteral()))));

        OWLNamedIndividual motherboardIndividual = dataFactory.getOWLNamedIndividual(IRI.create(GlobalStrings.baseIRI + motherboard.replace(" ", "_")));
        OWLNamedIndividual gpuIndividual = dataFactory.getOWLNamedIndividual(IRI.create(GlobalStrings.baseIRI + gpu.replace(" ", "_")));

        OWLClassExpression queryExpression = dataFactory.getOWLObjectIntersectionOf(
                classPSU,
                dataFactory.getOWLDataSomeValuesFrom(hasPower, powerRange),
                dataFactory.getOWLObjectHasValue(attachedPSU, motherboardIndividual),
                dataFactory.getOWLObjectHasValue(supplyGPU, gpuIndividual)
        );
        return getPSUResponseDTOs(queryExpression);
    }

    private List<PSUResponseDTO> getPSUResponseDTOs(OWLClassExpression classPSU) {
        Set<OWLNamedIndividual> individuals = reasoner.getInstances(classPSU, false).getFlattened();
        System.out.println("\nTotal number of retrieved individuals: " + individuals.size());
        List<PSUResponseDTO> psus = new ArrayList<>();
        for (OWLNamedIndividual individual : individuals) {
            psus.add(setSpec(individual));
        }
        return psus;
    }

    private PSUResponseDTO setSpec(OWLNamedIndividual individual) {
        PSUResponseDTO psuResponseDTO = new PSUResponseDTO();
        for (OWLDataPropertyAssertionAxiom assertion : ontologyManager.getOntology().getDataPropertyAssertionAxioms(individual)) {
            OWLDataProperty property = assertion.getProperty().asOWLDataProperty();
            if (hasName.equals(property)) {
                OWLLiteral value = assertion.getObject();
                psuResponseDTO.setName(value.getLiteral());
            } else if (hasCertificate.equals(property)) {
                OWLLiteral value = assertion.getObject();
                psuResponseDTO.setCertificate(value.getLiteral());
            } else if (hasDimensions.equals(property)) {
                OWLLiteral value = assertion.getObject();
                psuResponseDTO.setDimensions(value.getLiteral());
            }else if (hasPower.equals(property)) {
                OWLLiteral value = assertion.getObject();
                psuResponseDTO.setPower(Integer.parseInt(value.getLiteral()));
            }
        }
        return psuResponseDTO;
    }
}
