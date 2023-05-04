package com.owl.api.example.service;

import com.owl.api.example.additionalConfiguration.GlobalStrings;
import com.owl.api.example.dto.CPURequestDTO;
import com.owl.api.example.dto.CPUResponseDTO;
import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.vocab.OWLFacet;
import org.semanticweb.owlapi.vocab.XSDVocabulary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@Service
public class CPUService {

    private OntologyManager ontologyManager;

    private OWLDataProperty hasName;
    private OWLDataProperty hasTDP;
    private OWLDataProperty hasCores;
    private OWLDataProperty hasThreads;
    private OWLDataProperty hasClockSpeed;
    private OWLObjectProperty attachedCPU;
    private OWLObjectProperty compatibleGPU;
    private OWLObjectProperty hasSocket;
    private OWLClassExpression classCPU;
    private OWLOntologyManager manager;
    private OWLDataFactory dataFactory;
    private OWLReasonerFactory reasonerFactory;
    private OWLReasoner reasoner;


    public CPUService(OntologyManager ontologyManager) {
        this.ontologyManager = ontologyManager;
        manager = OWLManager.createOWLOntologyManager();
        dataFactory = manager.getOWLDataFactory();
        reasonerFactory = new ReasonerFactory();
        reasoner = reasonerFactory.createReasoner(this.ontologyManager.getOntology());
        hasName = dataFactory.getOWLDataProperty(IRI.create(GlobalStrings.baseIRI + "cpu_has_name"));
        hasTDP = dataFactory.getOWLDataProperty(IRI.create(GlobalStrings.baseIRI + "cpu_has_tdp_in_watts"));
        hasCores = dataFactory.getOWLDataProperty(IRI.create(GlobalStrings.baseIRI + "cpu_has_cores"));
        hasThreads = dataFactory.getOWLDataProperty(IRI.create(GlobalStrings.baseIRI + "cpu_has_threads"));
        hasClockSpeed = dataFactory.getOWLDataProperty(IRI.create(GlobalStrings.baseIRI + "cpu_has_clock_speed_in_ghz"));
        attachedCPU = dataFactory.getOWLObjectProperty(IRI.create(GlobalStrings.baseIRI + "cpu_attached_to"));
        compatibleGPU = dataFactory.getOWLObjectProperty(IRI.create(GlobalStrings.baseIRI + "unit1_is_compatible_with_unit2"));
        hasSocket = dataFactory.getOWLObjectProperty(IRI.create(GlobalStrings.baseIRI + "cpu_has_socket"));
        classCPU = dataFactory.getOWLClass(IRI.create(GlobalStrings.baseIRI + "CPU"));
    }

    public List<CPUResponseDTO> getAllCPUs(){
        return getCpuResponseDTOs(classCPU);
    }

    public List<CPUResponseDTO> getCPUUpgrades(String cpu, String motherboard, String gpu){

        OWLNamedIndividual cpuIndividual = dataFactory.getOWLNamedIndividual(IRI.create(GlobalStrings.baseIRI + cpu.replace(" ", "_")));

        Set<OWLLiteral> coreLiterals = reasoner.getDataPropertyValues(cpuIndividual, hasCores);
        OWLLiteral coreLiteral = coreLiterals.stream().findFirst().orElse(null);

        Set<OWLLiteral> threadLiterals = reasoner.getDataPropertyValues(cpuIndividual, hasThreads);
        OWLLiteral threadLiteral = threadLiterals.stream().findFirst().orElse(null);

        OWLDataRange coreRange = dataFactory.getOWLDatatypeRestriction(dataFactory.getIntegerOWLDatatype(),
                dataFactory.getOWLFacetRestriction(OWLFacet.MIN_EXCLUSIVE, dataFactory.getOWLLiteral(Integer.parseInt(coreLiteral.getLiteral()))));

        OWLDataRange threadRange = dataFactory.getOWLDatatypeRestriction(dataFactory.getIntegerOWLDatatype(),
                dataFactory.getOWLFacetRestriction(OWLFacet.MIN_EXCLUSIVE, dataFactory.getOWLLiteral(Integer.parseInt(threadLiteral.getLiteral()))));

        OWLNamedIndividual motherboardIndividual = dataFactory.getOWLNamedIndividual(IRI.create(GlobalStrings.baseIRI + motherboard.replace(" ", "_")));
        OWLNamedIndividual gpuIndividual = dataFactory.getOWLNamedIndividual(IRI.create(GlobalStrings.baseIRI + gpu.replace(" ", "_")));

        OWLClassExpression queryExpression = dataFactory.getOWLObjectIntersectionOf(
                classCPU,
                dataFactory.getOWLDataSomeValuesFrom(hasCores, coreRange),
                dataFactory.getOWLDataSomeValuesFrom(hasThreads, threadRange),
                dataFactory.getOWLObjectHasValue(attachedCPU, motherboardIndividual),
                dataFactory.getOWLObjectHasValue(compatibleGPU, gpuIndividual)
        );
        return getCpuResponseDTOs(queryExpression);
    }

    private List<CPUResponseDTO> getCpuResponseDTOs(OWLClassExpression classCPU) {
        Set<OWLNamedIndividual> individuals = reasoner.getInstances(classCPU, false).getFlattened();
        System.out.println("\nTotal number of retrieved individuals: " + individuals.size());
        List<CPUResponseDTO> cpus = new ArrayList<>();
        for (OWLNamedIndividual individual : individuals) {
            cpus.add(setSpec(individual));
        }
        return cpus;
    }

    public List<CPUResponseDTO> getCPUBySpec(CPURequestDTO cpu) {

        OWLDataRange coresRange = dataFactory.getOWLDatatypeRestriction(dataFactory.getIntegerOWLDatatype(),
                dataFactory.getOWLFacetRestriction(OWLFacet.MIN_INCLUSIVE, dataFactory.getOWLLiteral(cpu.getLowerCores())),
                dataFactory.getOWLFacetRestriction(OWLFacet.MAX_INCLUSIVE, dataFactory.getOWLLiteral(cpu.getHigherCores())));

        OWLDataRange threadsRange = dataFactory.getOWLDatatypeRestriction(dataFactory.getIntegerOWLDatatype(),
                dataFactory.getOWLFacetRestriction(OWLFacet.MIN_INCLUSIVE, dataFactory.getOWLLiteral(cpu.getLowerThreads())),
                dataFactory.getOWLFacetRestriction(OWLFacet.MAX_INCLUSIVE, dataFactory.getOWLLiteral(cpu.getHigherThreads())));

        OWLDataRange clockSpeedRange = dataFactory.getOWLDatatypeRestriction(dataFactory.getOWLDatatype(XSDVocabulary.DECIMAL.getIRI()),
                dataFactory.getOWLFacetRestriction(OWLFacet.MIN_INCLUSIVE, dataFactory.getOWLLiteral(String.valueOf(cpu.getLowerClockSpeed()), dataFactory.getOWLDatatype(XSDVocabulary.DECIMAL.getIRI()))),
                dataFactory.getOWLFacetRestriction(OWLFacet.MAX_INCLUSIVE, dataFactory.getOWLLiteral(String.valueOf(cpu.getHigherClockSpeed()), dataFactory.getOWLDatatype(XSDVocabulary.DECIMAL.getIRI()))));

        OWLDataRange TDPRange = dataFactory.getOWLDatatypeRestriction(dataFactory.getIntegerOWLDatatype(),
                dataFactory.getOWLFacetRestriction(OWLFacet.MIN_INCLUSIVE, dataFactory.getOWLLiteral(cpu.getLowerTDP())),
                dataFactory.getOWLFacetRestriction(OWLFacet.MAX_INCLUSIVE, dataFactory.getOWLLiteral(cpu.getHigherTDP())));

        OWLClassExpression queryExpression = dataFactory.getOWLObjectIntersectionOf(
                classCPU,
                dataFactory.getOWLDataSomeValuesFrom(hasCores, coresRange),
                dataFactory.getOWLDataSomeValuesFrom(hasThreads, threadsRange),
                dataFactory.getOWLDataSomeValuesFrom(hasTDP, TDPRange),
                dataFactory.getOWLDataSomeValuesFrom(hasClockSpeed, clockSpeedRange)
                );
        return getCpuResponseDTOs(queryExpression);
    }

    private CPUResponseDTO setSpec(OWLNamedIndividual individual) {
        CPUResponseDTO cpuResponseDTO = new CPUResponseDTO();
        for (OWLDataPropertyAssertionAxiom assertion : ontologyManager.getOntology().getDataPropertyAssertionAxioms(individual)) {
            OWLDataProperty property = assertion.getProperty().asOWLDataProperty();
            if (hasName.equals(property)) {
                OWLLiteral value = assertion.getObject();
                cpuResponseDTO.setName(value.getLiteral());
            } else if (hasTDP.equals(property)) {
                OWLLiteral value = assertion.getObject();
                cpuResponseDTO.setTDP(Integer.parseInt(value.getLiteral()));
            } else if (hasCores.equals(property)) {
                OWLLiteral value = assertion.getObject();
                cpuResponseDTO.setCores(Integer.parseInt(value.getLiteral()));
            } else if (hasThreads.equals(property)) {
                OWLLiteral value = assertion.getObject();
                cpuResponseDTO.setThreads(Integer.parseInt(value.getLiteral()));
            }
            else if (hasClockSpeed.equals(property)) {
                OWLLiteral value = assertion.getObject();
                cpuResponseDTO.setClockSpeed(Double.parseDouble(value.getLiteral()));
            }
        }
        Set<OWLNamedIndividual> sockets = reasoner.getObjectPropertyValues(individual, hasSocket).getFlattened();
        OWLNamedIndividual socket = sockets.stream().findFirst().orElse(null);
        cpuResponseDTO.setSocket(socket.getIRI().getShortForm().replace("_", " "));
        return cpuResponseDTO;
    }
}
