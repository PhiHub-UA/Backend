package deti.tqs.phihub.controllers.signage;

import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.HashMap;
import java.util.LinkedList;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/signage")
public class SignageController {
    

    private HashMap<String, Integer> ticketCalls = new HashMap<String, Integer>();
    private String[] ticketChars = {"A", "B", "C", "D"};

    private LinkedList<String> lastCalls = new LinkedList<String>();

    public SignageController() {
        Random rand = new Random();

        Integer nextA = rand.nextInt(70);
        ticketCalls.put("A", nextA);
        lastCalls.add("A" + nextA);
        Integer nextB = rand.nextInt(16);
        ticketCalls.put("B", nextB);
        lastCalls.add("B" + nextB);
        Integer nextC = rand.nextInt(20);
        ticketCalls.put("C", nextC);
        lastCalls.add("C" + nextC);
        Integer nextD = rand.nextInt(40);
        ticketCalls.put("D", nextD);
        lastCalls.add("D" + nextD);
        lastCalls.add("A" + (nextA - 1));

    }

    @GetMapping
    public ResponseEntity<String> getLoggedInStaff() {

        String lastFiveCalled = "[\"" + lastCalls.get(lastCalls.size()-1) +
                                "\", \"" + lastCalls.get(lastCalls.size()-2) +
                                "\", \"" + lastCalls.get(lastCalls.size()-3) +
                                "\", \"" + lastCalls.get(lastCalls.size()-4) +
                                "\", \"" + lastCalls.get(lastCalls.size()-5) + "\"]";

        return ResponseEntity.ok("{\"latest\":\"" + lastCalls.getLast() + "\", \"lastFiveCalls\":" + lastFiveCalled + "}");
    }

    @PostMapping
    public ResponseEntity<String> callTicket() {

        Random rand = new Random();

        int nextLineID = rand.nextInt(ticketCalls.size());
        String nextLineChar = ticketChars[nextLineID];

        lastCalls.add(nextLineChar + ticketCalls.get(nextLineChar).toString());

        ticketCalls.put(nextLineChar, ticketCalls.get(nextLineChar) + 1);

        return ResponseEntity.ok("{\"message\":\"Next ticket updated sucessfully\"}");

    }
}
