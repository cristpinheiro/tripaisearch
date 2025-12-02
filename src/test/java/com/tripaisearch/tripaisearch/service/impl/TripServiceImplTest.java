package com.tripaisearch.tripaisearch.service.impl;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import com.tripaisearch.tripaisearch.entity.Trip;
import com.tripaisearch.tripaisearch.repository.TripRepository;
import com.tripaisearch.tripaisearch.service.AiClient;

@ExtendWith(MockitoExtension.class)
public class TripServiceImplTest {

    @Mock
    private TripRepository tripRepository;

    @Mock
    private AiClient aiClient;

    private TripServiceImpl tripService;

    @BeforeEach
    void setUp() {
        tripService = new TripServiceImpl(tripRepository, aiClient);
    }

    @Test
    void testDeleteById() {
        Long id = 1L;

        tripService.deleteById(id);

        verify(tripRepository).deleteById(id);
    }

    @Test
    void testDeleteById_WithNullId_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> tripService.deleteById(null));
    }

    @Test
    void testFindByPromptWithNullPrompt() {
        Trip trip1 = new Trip();
        Trip trip2 = new Trip();
        List<Trip> expectedTrips = Arrays.asList(trip1, trip2);
        when(tripRepository.findAll()).thenReturn(expectedTrips);

        List<Trip> result = tripService.findByPrompt(null);

        assertEquals(expectedTrips, result);
        verify(tripRepository).findAll();
    }

    @SuppressWarnings("unchecked")
    @Test
    void testFindByPrompt_WithAiFilters() {
        String prompt = "Trips with more than four passengers in region 100 during the month 11 of the year 2018.";
        Trip trip1 = new Trip();
        Trip trip2 = new Trip();
        List<Trip> expectedTrips = Arrays.asList(trip1, trip2);
        when(tripRepository.findAll(any(Specification.class))).thenReturn(expectedTrips);
        when(aiClient.generate(anyString())).thenReturn(
                "{ \"start\": \"2018-11-01 00:00:00\", \"end\": \"2018-11-30 23:59:59\", \"minPassengers\": 4, \"maxPassengers\": 4, \"pickupZone\": 100 }");

        List<Trip> result = tripService.findByPrompt(prompt);

        assertEquals(expectedTrips, result);
        verify(tripRepository).findAll(any(Specification.class));
        verify(aiClient).generate(contains(prompt));
    }

    @SuppressWarnings("unchecked")
    @Test
    void testFindByPrompt_WithNoRecognizedAiFilters() {
        String prompt = "Trips with more than four passengers in region 100 during the month 11 of the year 2018.";
        Trip trip1 = new Trip();
        Trip trip2 = new Trip();
        List<Trip> expectedTrips = Arrays.asList(trip1, trip2);
        when(tripRepository.findAll(any(Specification.class))).thenReturn(expectedTrips);
        when(aiClient.generate(anyString())).thenReturn(
                "{ \"start\": null, \"end\": null, \"minPassengers\": null, \"maxPassengers\": null, \"pickupZone\": null }");

        List<Trip> result = tripService.findByPrompt(prompt);

        assertEquals(expectedTrips, result);
        verify(tripRepository).findAll(any(Specification.class));
        verify(aiClient).generate(contains(prompt));
    }

    @Test
    void testFindByPrompt_ShouldThrowRuntimeException() {
        assertThrows(RuntimeException.class, () -> tripService.findByPrompt(""));
    }

    @Test
    void testFindById() {
        Long id = 1L;
        Trip trip = new Trip();
        when(tripRepository.findById(id)).thenReturn(Optional.of(trip));

        Optional<Trip> result = tripService.findById(id);

        assertTrue(result.isPresent());
        assertEquals(trip, result.get());
        verify(tripRepository).findById(id);
    }

    @Test
    void testFindById_WithNullId_ShouldReturnEmpty() {
        Optional<Trip> result = tripService.findById(null);

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindById_NotFound_ShouldReturnEmpty() {
        Long id = 1L;
        when(tripRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Trip> result = tripService.findById(id);

        assertTrue(result.isEmpty());
        verify(tripRepository).findById(id);
    }

    @Test
    void testSave() {
        Trip trip = new Trip();
        Trip savedTrip = new Trip();
        when(tripRepository.save(trip)).thenReturn(savedTrip);

        Trip result = tripService.save(trip);

        assertEquals(savedTrip, result);
        verify(tripRepository).save(trip);
    }

    @Test
    void testSave_WithNullTrip_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> tripService.save(null));
    }
}
