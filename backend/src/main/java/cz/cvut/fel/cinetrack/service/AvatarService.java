/*
 * Created by minmin_tranova on 31.10.2025
 */

package cz.cvut.fel.cinetrack.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class AvatarService {

    private List<String> availableAvatars = new ArrayList<>();
    private final Random random = new Random();

    @PostConstruct
    @Transactional
    public void init() {
        loadAvailableAvatars();
    }

    private void loadAvailableAvatars() {
        availableAvatars = Arrays.asList(
                "static/avatars/Avatar01.png",
                "static/avatars/Avatar03.png",
                "static/avatars/Avatar04.png",
                "static/avatars/Avatar05.png",
                "static/avatars/Avatar06.png",
                "static/avatars/Avatar07.png",
                "static/avatars/Avatar08.png",
                "static/avatars/Avatar09.png",
                "static/avatars/Avatar10.png",
                "static/avatars/Avatar11.png",
                "static/avatars/Avatar12.png",
                "static/avatars/Avatar13.png",
                "static/avatars/Avatar14.png",
                "static/avatars/Avatar15.png",
                "static/avatars/Avatar16.png",
                "static/avatars/Avatar17.png",
                "static/avatars/Avatar18.png",
                "static/avatars/Avatar19.png",
                "static/avatars/Avatar21.png"
        );
    }

    @Transactional
    public String getRandomAvatar() {
        if (availableAvatars.isEmpty()) {
            return "static/avatars/Avatar01.png";
        }
        return availableAvatars.get(random.nextInt(availableAvatars.size()));
    }
}
