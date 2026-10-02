package com.chris.uniconnect.Model.Dto;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class SkillsRequest {

    private Set<Integer> tecnologias = new HashSet<>();

    private Set<Integer> aptitudes = new HashSet<>();
}
