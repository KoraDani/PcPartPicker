package com.example.pcconf;

public class PickedPart {
    private String cpu;
    private String motherBoard;
    private String gpu;
    private String ram;
    private String storage;

    public PickedPart(String cpu, String motherBoard, String gpu, String ram, String storage) {
        this.cpu = cpu;
        this.motherBoard = motherBoard;
        this.gpu = gpu;
        this.ram = ram;
        this.storage = storage;
    }

    public String getCpu() {
        return cpu;
    }

    public String getMotherBoard() {
        return motherBoard;
    }

    public String getGpu() {
        return gpu;
    }

    public String getRam() {
        return ram;
    }

    public String getStorage() {
        return storage;
    }
}
