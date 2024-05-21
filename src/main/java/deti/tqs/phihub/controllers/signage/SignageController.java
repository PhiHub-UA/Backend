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
    

    private HashMap<String, Integer> ticketCalls = new HashMap<>();
    private String[] ticketChars = {"A", "B", "C", "D"};

    private LinkedList<String> lastCalls = new LinkedList<>();
    private String nextNum = "---";

    Random rand = new Random();

    public SignageController() {
        Integer nextA = rand.nextInt(2, 70);
        ticketCalls.put("A", nextA + 1);
        lastCalls.add("[\"A" + nextA + "\", 1]");
        Integer nextB = rand.nextInt(16);
        ticketCalls.put("B", nextB);
        lastCalls.add("[\"B" + nextB + "\", 3]");
        Integer nextC = rand.nextInt(20);
        ticketCalls.put("C", nextC);
        lastCalls.add("[\"C" + nextC + "\", 6]");
        Integer nextD = rand.nextInt(40);
        ticketCalls.put("D", nextD);
        lastCalls.add("[\"D" + nextD + "\", 4]");
        lastCalls.add("[\"A" + (nextA + 1) + "\", 7]");


        int nextLineID = rand.nextInt(ticketCalls.size());
        String nextLineChar = ticketChars[nextLineID];
        ticketCalls.put(nextLineChar, ticketCalls.get(nextLineChar) + 1);

        nextNum = nextLineChar + ticketCalls.get(nextLineChar).toString();
    }

    @GetMapping
    public ResponseEntity<String> getLoggedInStaff() {

        String lastCalled = "\"1\":" + lastCalls.get(lastCalls.size()-1) +
                          ", \"2\":" + lastCalls.get(lastCalls.size()-2) +
                          ", \"3\":" + lastCalls.get(lastCalls.size()-3) +
                          ", \"4\":" + lastCalls.get(lastCalls.size()-4) +
                          ", \"5\":" + lastCalls.get(lastCalls.size()-5);

        return ResponseEntity.ok("{\"nextnum\":\"" + nextNum + "\", " + lastCalled + "}");
    }

    @PostMapping
    public ResponseEntity<String> callTicket() {

        lastCalls.add("[\"" + nextNum + "\", " + (rand.nextInt(7) + 1) + "]");

        int nextLineID = rand.nextInt(ticketCalls.size());
        String nextLineChar = ticketChars[nextLineID];
        ticketCalls.put(nextLineChar, ticketCalls.get(nextLineChar) + 1);

        nextNum = nextLineChar + ticketCalls.get(nextLineChar).toString();

        return ResponseEntity.ok("{\"message\":\"Next ticket updated sucessfully\"}");
    }
}
