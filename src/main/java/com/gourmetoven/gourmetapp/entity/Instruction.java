package com.gourmetoven.gourmetapp.entity;

import javax.persistence.*;

@Entity
@SequenceGenerator(name = "INS_SEQ")
public class Instruction {
    @Id
    @Column(name = "instruction_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "INS_SEQ")
    private Integer instructionId;

    @Column(name = "instruction")
    private String instruction;

    public Instruction() {
    }

    public Instruction(Integer instructionId, String instruction) {
        this.instructionId = instructionId;
        this.instruction = instruction;
    }

    public Integer getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(Integer instructionId) {
        this.instructionId = instructionId;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    @Override
    public String toString() {
        return "Instruction{" +
                "instruction_id=" + instructionId +
                ", instruction='" + instruction + '\'' +
                '}';
    }
}
