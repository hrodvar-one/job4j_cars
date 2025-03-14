package ru.job4j.cars.service.historyowner;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.HistoryOwner;
import ru.job4j.cars.repository.historyowner.HistoryOwnerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryOwnerService {

    private final HistoryOwnerRepository historyOwnerRepository;

    public List<HistoryOwner> getAllHistoryOwner() {
        return historyOwnerRepository.getAllHistoryOwner();
    }
}
