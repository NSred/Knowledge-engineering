package com.owl.api.example.service;

import com.owl.api.example.dto.GPURequestDTO;
import com.owl.api.example.dto.GPUResponseDTO;
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
public class GPUService {

    private OntologyManager ontologyManager;

    private OWLDataProperty hasName;
    private OWLDataProperty hasTDP;
    private OWLDataProperty hasVideoMemory;
    private OWLDataProperty hasCoreClock;
    private OWLDataProperty hasMemoryClock;
    private OWLObjectProperty attachedGPU;
    private OWLObjectProperty suppliedByPSU;
    private OWLObjectProperty compatibleCPU;
    private OWLClassExpression classGPU;
    private static String baseIRI = "http://www.semanticweb.org/administrator/ontologies/2023/2/untitled-ontology-3#";
    private OWLOntologyManager manager;
    private OWLDataFactory dataFactory;
    private OWLReasonerFactory reasonerFactory;
    private OWLReasoner reasoner;


    public GPUService() throws OWLOntologyCreationException, OWLOntologyStorageException {
        ontologyManager = new OntologyManager();
        manager = OWLManager.createOWLOntologyManager();
        dataFactory = manager.getOWLDataFactory();
        reasonerFactory = new ReasonerFactory();
        reasoner = reasonerFactory.createReasoner(this.ontologyManager.getOntology());
        hasName = dataFactory.getOWLDataProperty(IRI.create(baseIRI + "gpu_has_name"));
        hasTDP = dataFactory.getOWLDataProperty(IRI.create(baseIRI + "gpu_has_tdp_in_watts"));
        hasCoreClock = dataFactory.getOWLDataProperty(IRI.create(baseIRI + "gpu_has_core_clock_in_mhz"));
        hasMemoryClock = dataFactory.getOWLDataProperty(IRI.create(baseIRI + "gpu_has_memory_clock"));
        hasVideoMemory = dataFactory.getOWLDataProperty(IRI.create(baseIRI + "gpu_has_video_memory_in_mb"));
        attachedGPU = dataFactory.getOWLObjectProperty(IRI.create(baseIRI + "graphic_card_attached_to"));
        suppliedByPSU = dataFactory.getOWLObjectProperty(IRI.create(baseIRI + "graphic_card_power_supply"));
        compatibleCPU = dataFactory.getOWLObjectProperty(IRI.create(baseIRI + "unit1_is_compatible_with_unit2"));
        classGPU = dataFactory.getOWLClass(IRI.create(baseIRI + "Graphic_card"));
    }

    public List<GPUResponseDTO> getAllGPUs(){
        return getGpuResponseDTOs(classGPU);
    }

    public List<GPUResponseDTO> getGPUUpgrades(String gpu, String motherboard, String cpu, String psu){

        OWLNamedIndividual gpuIndividual = dataFactory.getOWLNamedIndividual(IRI.create(baseIRI + gpu.replace(" ", "_")));

        Set<OWLLiteral> memoryLiterals = reasoner.getDataPropertyValues(gpuIndividual, hasVideoMemory);
        OWLLiteral memoryLiteral = memoryLiterals.stream().findFirst().orElse(null);

        OWLDataRange memoryRange = dataFactory.getOWLDatatypeRestriction(dataFactory.getIntegerOWLDatatype(),
                dataFactory.getOWLFacetRestriction(OWLFacet.MIN_EXCLUSIVE, dataFactory.getOWLLiteral(Integer.parseInt(memoryLiteral.getLiteral()))));

        OWLNamedIndividual motherboardIndividual = dataFactory.getOWLNamedIndividual(IRI.create(baseIRI + motherboard.replace(" ", "_")));
        OWLNamedIndividual cpuIndividual = dataFactory.getOWLNamedIndividual(IRI.create(baseIRI + cpu.replace(" ", "_")));
        OWLNamedIndividual psuIndividual = dataFactory.getOWLNamedIndividual(IRI.create(baseIRI + psu.replace(" ", "_")));

        OWLClassExpression queryExpression = dataFactory.getOWLObjectIntersectionOf(
                classGPU,
                dataFactory.getOWLDataSomeValuesFrom(hasVideoMemory, memoryRange),
                dataFactory.getOWLObjectHasValue(attachedGPU, motherboardIndividual),
                dataFactory.getOWLObjectHasValue(suppliedByPSU, psuIndividual),
                dataFactory.getOWLObjectHasValue(compatibleCPU, cpuIndividual)
        );
        return getGpuResponseDTOs(queryExpression);
    }
    private List<GPUResponseDTO> getGpuResponseDTOs(OWLClassExpression classGPU) {
        Set<OWLNamedIndividual> individuals = reasoner.getInstances(classGPU, false).getFlattened();
        List<GPUResponseDTO> gpus = new ArrayList<>();
        for (OWLNamedIndividual individual : individuals) {
            gpus.add(setSpec(individual));
        }
        return gpus;
    }

    public List<GPUResponseDTO> getGPUBySpec(GPURequestDTO gpu) {

        OWLDataRange coreClockRange = dataFactory.getOWLDatatypeRestriction(dataFactory.getIntegerOWLDatatype(),
                dataFactory.getOWLFacetRestriction(OWLFacet.MIN_INCLUSIVE, dataFactory.getOWLLiteral(gpu.getLowerCoreClock())),
                dataFactory.getOWLFacetRestriction(OWLFacet.MAX_INCLUSIVE, dataFactory.getOWLLiteral(gpu.getHigherCoreClock())));

        OWLDataRange memoryClockRange = dataFactory.getOWLDatatypeRestriction(dataFactory.getIntegerOWLDatatype(),
                dataFactory.getOWLFacetRestriction(OWLFacet.MIN_INCLUSIVE, dataFactory.getOWLLiteral(gpu.getLowerMemoryClock())),
                dataFactory.getOWLFacetRestriction(OWLFacet.MAX_INCLUSIVE, dataFactory.getOWLLiteral(gpu.getHigherMemoryClock())));

        OWLDataRange videoMemoryRange = dataFactory.getOWLDatatypeRestriction(dataFactory.getIntegerOWLDatatype(),
                dataFactory.getOWLFacetRestriction(OWLFacet.MIN_INCLUSIVE, dataFactory.getOWLLiteral(gpu.getLowerVideoMemory())),
                dataFactory.getOWLFacetRestriction(OWLFacet.MAX_INCLUSIVE, dataFactory.getOWLLiteral(gpu.getHigherVideoMemory())));

        OWLDataRange TDPRange = dataFactory.getOWLDatatypeRestriction(dataFactory.getIntegerOWLDatatype(),
                dataFactory.getOWLFacetRestriction(OWLFacet.MIN_INCLUSIVE, dataFactory.getOWLLiteral(gpu.getLowerTDP())),
                dataFactory.getOWLFacetRestriction(OWLFacet.MAX_INCLUSIVE, dataFactory.getOWLLiteral(gpu.getHigherTDP())));

        OWLClassExpression queryExpression = dataFactory.getOWLObjectIntersectionOf(
                classGPU,
                dataFactory.getOWLDataSomeValuesFrom(hasCoreClock, coreClockRange),
                dataFactory.getOWLDataSomeValuesFrom(hasMemoryClock, memoryClockRange),
                dataFactory.getOWLDataSomeValuesFrom(hasTDP, TDPRange),
                dataFactory.getOWLDataSomeValuesFrom(hasVideoMemory, videoMemoryRange)
                );
        return getGpuResponseDTOs(queryExpression);
    }

    private GPUResponseDTO setSpec(OWLNamedIndividual individual) {
        GPUResponseDTO gpuResponseDTO = new GPUResponseDTO();
        for (OWLDataPropertyAssertionAxiom assertion : ontologyManager.getOntology().getDataPropertyAssertionAxioms(individual)) {
            OWLDataProperty property = assertion.getProperty().asOWLDataProperty();
            if (hasName.equals(property)) {
                OWLLiteral value = assertion.getObject();
                gpuResponseDTO.setName(value.getLiteral());
            } else if (hasTDP.equals(property)) {
                OWLLiteral value = assertion.getObject();
                gpuResponseDTO.setTDP(Integer.parseInt(value.getLiteral()));
            } else if (hasVideoMemory.equals(property)) {
                OWLLiteral value = assertion.getObject();
                gpuResponseDTO.setVideoMemory(Integer.parseInt(value.getLiteral()));
            } else if (hasMemoryClock.equals(property)) {
                OWLLiteral value = assertion.getObject();
                gpuResponseDTO.setMemoryClock(Integer.parseInt(value.getLiteral()));
            }
            else if (hasCoreClock.equals(property)) {
                OWLLiteral value = assertion.getObject();
                gpuResponseDTO.setCoreClock(Integer.parseInt(value.getLiteral()));
            }
        }
        return gpuResponseDTO;
    }
}
