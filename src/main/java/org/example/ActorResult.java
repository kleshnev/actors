package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ActorResult {
    private List<AddOrderMessage> orderSequence;
    private String name;
}
