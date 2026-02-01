package com.example.project_employee.service;

import com.example.project_employee.dto.ClientRequestDto;
import com.example.project_employee.dto.ClientResponseDto;
import com.example.project_employee.dto.OrderSimpleDto;
import com.example.project_employee.entity.ClientEntity;
import com.example.project_employee.mapper.ClientMapper;
import com.example.project_employee.mapper.OrderMapper;
import com.example.project_employee.repository.ClientRepository;
import com.example.project_employee.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ClientService {

    private final ClientRepository clientRepository;
    private final OrderRepository orderRepository;
    private final ClientMapper clientMapper;
    private final OrderMapper orderMapper;

    public ClientResponseDto createClient(ClientRequestDto requestDto) {
        log.info("Создание нового клиента: {}", requestDto.getEmail());

        // Проверка уникальности email
        if (clientRepository.existsByEmail(requestDto.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email '" + requestDto.getEmail() + "' уже используется"
            );
        }

        // Проверка уникальности телефона
        if (clientRepository.existsByPhone(requestDto.getPhone())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Телефон '" + requestDto.getPhone() + "' уже используется"
            );
        }

        ClientEntity clientEntity = clientMapper.toEntity(requestDto);
        ClientEntity saved = clientRepository.save(clientEntity);
        log.info("Создан клиент с ID: {}", saved.getId());

        return createClientResponseDto(saved);
    }

    @Transactional(readOnly = true)
    public ClientResponseDto getClientById(Long id) {
        log.info("Получение клиента по ID: {}", id);

        ClientEntity clientEntity = clientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Клиент с ID " + id + " не найден"
                ));

        return createClientResponseDto(clientEntity);
    }

    public ClientResponseDto updateClient(Long id, ClientRequestDto requestDto) {
        log.info("Обновление клиента с ID: {}", id);

        ClientEntity clientEntity = clientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Клиент с ID " + id + " не найден"
                ));

        // Проверка уникальности email (если изменился)
        if (!clientEntity.getEmail().equals(requestDto.getEmail()) &&
                clientRepository.existsByEmailAndIdNot(requestDto.getEmail(), id)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email '" + requestDto.getEmail() + "' уже используется другим клиентом"
            );
        }

        // Проверка уникальности телефона (если изменился)
        if (!clientEntity.getPhone().equals(requestDto.getPhone()) &&
                clientRepository.existsByPhoneAndIdNot(requestDto.getPhone(), id)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Телефон '" + requestDto.getPhone() + "' уже используется другим клиентом"
            );
        }

        clientMapper.updateEntity(clientEntity, requestDto);
        ClientEntity updated = clientRepository.save(clientEntity);
        log.info("Обновлен клиент с ID: {}", updated.getId());

        return createClientResponseDto(updated);
    }

    public void deleteClient(Long id) {
        log.info("Удаление клиента с ID: {}", id);

        if (!clientRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Клиент с ID " + id + " не найден"
            );
        }

        // При удалении клиента заказы остаются, но теряют связь
        clientRepository.deleteById(id);
        log.info("Удален клиент с ID: {}", id);
    }

    @Transactional(readOnly = true)
    public Page<ClientResponseDto> getAllClients(Pageable pageable) {
        log.info("Получение всех клиентов с пагинацией: page={}, size={}",
                pageable.getPageNumber(), pageable.getPageSize());

        return clientRepository.findAll(pageable)
                .map(this::createClientResponseDto);
    }

    @Transactional(readOnly = true)
    public List<ClientResponseDto> getAllClients() {
        log.info("Получение всех клиентов без пагинации");

        return clientRepository.findAll().stream()
                .map(this::createClientResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<ClientResponseDto> getClientsWithFilters(
            String firstName,
            String lastName,
            String email,
            String phone,
            Pageable pageable) {

        log.info("Фильтрация клиентов с параметрами: firstName={}, lastName={}, email={}, phone={}",
                firstName, lastName, email, phone);

        Page<ClientEntity> clientEntities = clientRepository.findByAllFilters(
                firstName,
                lastName,
                email,
                phone,
                pageable
        );

        return clientEntities.map(this::createClientResponseDto);
    }

    @Transactional(readOnly = true)
    public List<ClientResponseDto> searchClientsByFirstName(String firstName) {
        log.info("Поиск клиентов по имени: {}", firstName);

        if (!StringUtils.hasText(firstName)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Имя для поиска не указано"
            );
        }

        return clientRepository.findByFirstNameContainingIgnoreCase(firstName).stream()
                .map(this::createClientResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ClientResponseDto> searchClientsByLastName(String lastName) {
        log.info("Поиск клиентов по фамилии: {}", lastName);

        if (!StringUtils.hasText(lastName)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Фамилия для поиска не указана"
            );
        }

        return clientRepository.findByLastNameContainingIgnoreCase(lastName).stream()
                .map(this::createClientResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return clientRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public ClientResponseDto getClientByEmail(String email) {
        log.info("Поиск клиента по email: {}", email);

        ClientEntity clientEntity = clientRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Клиент с email '" + email + "' не найден"
                ));

        return createClientResponseDto(clientEntity);
    }

    @Transactional(readOnly = true)
    public ClientResponseDto getClientByPhone(String phone) {
        log.info("Поиск клиента по телефону: {}", phone);

        ClientEntity clientEntity = clientRepository.findByPhone(phone)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Клиент с телефоном '" + phone + "' не найден"
                ));

        return createClientResponseDto(clientEntity);
    }

    @Transactional(readOnly = true)
    public long countClients() {
        return clientRepository.count();
    }

    // Вспомогательный метод для создания ClientResponseDto с заказами
    private ClientResponseDto createClientResponseDto(ClientEntity clientEntity) {
        // Создаем базовый DTO через маппер
        ClientResponseDto clientDto = clientMapper.toResponseDto(clientEntity);

        // Получаем заказы клиента и добавляем их в DTO
        List<OrderSimpleDto> orders = orderRepository.findByClientId(clientEntity.getId()).stream()
                .map(orderMapper::toSimpleDto)
                .toList();

        clientDto.setOrders(orders);
        return clientDto;
    }
}