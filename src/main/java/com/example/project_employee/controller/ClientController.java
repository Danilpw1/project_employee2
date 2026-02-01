package com.example.project_employee.controller;

import com.example.project_employee.dto.ClientRequestDto;
import com.example.project_employee.dto.ClientResponseDto;
import com.example.project_employee.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientResponseDto> createClient(@Valid @RequestBody ClientRequestDto requestDto) {
        ClientResponseDto created = clientService.createClient(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDto> getClient(@PathVariable Long id) {
        ClientResponseDto client = clientService.getClientById(id);
        return ResponseEntity.ok(client);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDto> updateClient(
            @PathVariable Long id,
            @Valid @RequestBody ClientRequestDto requestDto) {
        ClientResponseDto updated = clientService.updateClient(id, requestDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<ClientResponseDto>> getAllClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ClientResponseDto> clients = clientService.getAllClients(pageable);
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ClientResponseDto>> getAllClientsWithoutPagination() {
        List<ClientResponseDto> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<ClientResponseDto>> getClientsWithFilters(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ClientResponseDto> clients = clientService.getClientsWithFilters(
                firstName,
                lastName,
                email,
                phone,
                pageable
        );

        return ResponseEntity.ok(clients);
    }

    @GetMapping("/search/first-name")
    public ResponseEntity<List<ClientResponseDto>> searchClientsByFirstName(
            @RequestParam String firstName) {
        List<ClientResponseDto> clients = clientService.searchClientsByFirstName(firstName);
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/search/last-name")
    public ResponseEntity<List<ClientResponseDto>> searchClientsByLastName(
            @RequestParam String lastName) {
        List<ClientResponseDto> clients = clientService.searchClientsByLastName(lastName);
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ClientResponseDto> getClientByEmail(@PathVariable String email) {
        ClientResponseDto client = clientService.getClientByEmail(email);
        return ResponseEntity.ok(client);
    }

    @GetMapping("/check-email/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        boolean exists = clientService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }
}