package com.gourmetoven.gourmetapp.repository;

import com.gourmetoven.gourmetapp.entity.Instruction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InstructionRepository extends JpaRepository<Instruction, Integer>, JpaSpecificationExecutor<Instruction> {
    List<Instruction> findAllByInstructionIn(List<String> instructionNames);

    @Query("SELECT MAX(ins.instructionId) FROM Instruction ins")
    Long findMaxInstructionId();
}
