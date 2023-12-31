package com.kino_cms.service;

import com.kino_cms.dto.FilmDTO;
import com.kino_cms.dto.UserDTO;
import com.kino_cms.entity.FeedPage;
import com.kino_cms.entity.User;
import com.kino_cms.enums.City;
import com.kino_cms.enums.FeedType;
import com.kino_cms.enums.Language;
import com.kino_cms.enums.Role;
import com.kino_cms.repository.FeedPageRepo;
import com.kino_cms.repository.FilmRepo;
import com.kino_cms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class StatisticService {

    private final UserRepository userRepository;
    private final FilmRepo filmRepo;
    private final FeedPageRepo feedPageRepo;


    public Integer getUserCount() {
        log.info("-> start execution method getUserCount()");
        List<User> all = userRepository.findAll();
        log.info("-> exit from method getUserCount()");
        return all.size();
    }

    public List<FeedPage> getActivePromotions() {
        log.info("-> start execution method getActivePromotions()");
        List<FeedPage> feedPagesByFeedType = feedPageRepo.findFeedPagesByFeedType(FeedType.PROMOTION);
        List<FeedPage> activeFeedPageList = feedPagesByFeedType
                .stream()
                .filter(FeedPage::getIsActivate)
                .toList();
        log.info("-> exit from method getActivePromotions() promotions count: " + activeFeedPageList.size());
        return activeFeedPageList;
    }

    public Integer getCountFilmIsReleasedNow() {
        log.info("-> start execution method getCountFilmIsReleasedNow()");
        List<FilmDTO> allFilmIsReleasedNow = filmRepo.getAllFilmIsReleasedNow(Language.UKRAINIAN);
        log.info("-> exit from method getCountFilmIsReleasedNow() film count: " + allFilmIsReleasedNow.size());
        return allFilmIsReleasedNow.size();
    }

    public Map<String, Integer> getUsersGender() {
        log.info("-> start execution method getUsersGender()");
        List<User> all = userRepository.findAll();
        log.info("-> getUsersGender > exception admin on list user");
        List<User> userWithoutAdmin = all
                .stream()
                .filter(userDTO -> userDTO.getRole().equals(Role.ROLE_USER))
                .filter(user -> user.getUserDetails().getSex() != null)
                .toList();
        log.info("-> getUsersGender > filter users by sex(Male)");
        List<User> males = userWithoutAdmin
                .stream()
                .filter(user -> user.getUserDetails().getSex().equals("Male"))
                .toList();
        log.info("-> getUsersGender > filter users by sex(Female)");
        List<User> females = userWithoutAdmin
                .stream()
                .filter(user -> user.getUserDetails().getSex().equals("Female"))
                .toList();

        Map<String, Integer> genderMap = new HashMap<>();
        genderMap.put("Male", males.size());
        genderMap.put("Female", females.size());
        log.info(String.format("-> exit from method getUsersGender(), return Map has Male: %s, Female: %s users",
                males.size(), females.size()));
        return genderMap;
    }


    public Map<String, Integer> getCityStat() {
        Map<String, Integer> cityOnCount = new HashMap<>();
        List<UserDTO> userDTOList = userRepository.getUserDTOList()
                .stream()
                .filter(userDTO -> userDTO.getCity() != null)
                .toList();
        List<City> cityList = Arrays.stream(City.values()).toList();

        for (City city : cityList) {
            long countUsers = userDTOList.stream().filter(userDTO -> userDTO.getCity() == city).count();
            if (countUsers > 0) {
                cityOnCount.put(city.getDescription(), (int) countUsers);
            }
        }
        return cityOnCount;
    }

    public Integer getUserCountForLastWeek() {
        List<User> all = userRepository.findAll().stream().filter(user -> user.getCreateTime() != null && user.getCreateTime().length() > 1).toList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        long count = all.stream().filter(user -> LocalDateTime.parse(user.getCreateTime(), formatter).isAfter(LocalDateTime.now().minusDays(7))).count();
        return (int) count;
    }
}
