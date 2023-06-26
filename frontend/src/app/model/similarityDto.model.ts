export class SimilarityDto{
    cpuName : string = ''
    cpuTDP : number = 0
    cpuClockSpeed : number = 0
    cpuCores : number = 0;
    cpuThreads : number = 0;
    cpuSocket : string = '';
    gpuName : string = '';
    gpuTDP : number = 0;
    gpuCoreClock : number = 0;
    gpuMemoryClock : number = 0;
    gpuVideoMemory : number = 0;
    motherboardFormFactor : string = '';
    motherboardChipset : string = '';
    motherboardSocket : string = '';
    ramType : string = '';
    ramLatency : number = 0;
    ramVoltage : number = 0;
    ramCapacity : number = 0;
    psuPower : number = 0;
    idAttribute : string = '';

    constructor(obj?: any) {
        if (obj) {
          this.cpuName = obj.cpuName;
          this.cpuTDP = obj.cpuTDP;
          this.cpuClockSpeed = obj.cpuClockSpeed;
          this.cpuCores = obj.cpuCores;
          this.cpuThreads = obj.cpuThreads;
          this.cpuSocket = obj.cpuSocket;
          this.gpuName = obj.gpuName;
          this.gpuTDP = obj.gpuTDP;
          this.gpuCoreClock = obj.gpuCoreClock;
          this.gpuMemoryClock = obj.gpuMemoryClock;
          this.gpuVideoMemory = obj.gpuVideoMemory;
          this.motherboardFormFactor = obj.motherboardFormFactor;
          this.motherboardChipset = obj.motherboardChipset;
          this.motherboardSocket = obj.motherboardSocket;
          this.ramType = obj.ramType;
          this.ramLatency = obj.ramLatency;
          this.ramVoltage = obj.ramVoltage;
          this.ramCapacity = obj.ramCapacity;
          this.psuPower = obj.psuPower;
          this.idAttribute = obj.idAttribute;
        }
      }
}