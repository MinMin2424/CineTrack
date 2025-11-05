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
                "static/avatars/Avatar06.png"
        );
    }

    @Transactional
    public String getRandomAvatar() {
        if (availableAvatars.isEmpty()) {
            return "static/avatars/Avatar01.png";
        }
        return availableAvatars.get(random.nextInt(availableAvatars.size()));
    }

    @Transactional
    public List<String> getAvailableAvatars() {
        return new ArrayList<>(availableAvatars);
    }

    @Transactional
    public int getAvailableAvatarsSize() {
        return availableAvatars.size();
    }

    /*private final ResourceLoader resourceLoader;

    public AvatarService(ResourceLoader resourceLoader) {
        this.availableAvatars = loadAvailableAvatars();
        this.random = new Random();
        this.resourceLoader = resourceLoader;
    }

    private List<String> loadAvailableAvatars() {
        List<String> avatars = new ArrayList<>();

        try {
            Resource avatarDir = resourceLoader.getResource("classpath:avatars");
            if (avatarDir.exists()) {
                File directory = avatarDir.getFile();
                if (directory.isDirectory()) {
                    File[] files = directory.listFiles((dir, name) -> name.endsWith(".png"));
                    if (files != null) {
                        for (File file : files) {
                            avatars.add("static/avatars/" + file.getName());
                        }
                    }
                }

            }
        } catch (IOException e) {
            avatars = loadFromClasspath();
        }
        if (avatars.isEmpty()) {
            avatars = getDefaultAvatars();
        }
        return avatars;
    }

    private List<String> loadFromClasspath() {
        List<String> avatars = new ArrayList<>();

        try {
            ClassLoader classLoader = resourceLoader.getClassLoader();
            java.net.URL avatarsUrl = classLoader.getResource("static/avatars");

            if (avatarsUrl != null) {
                File directory = new File(avatarsUrl.getFile());
                if (directory.isDirectory()) {
                    File[] files = directory.listFiles((dir, name) -> name.endsWith(".png"));
                    if (files != null) {
                        for (File file : files) {
                            avatars.add("static/avatars/" + file.getName());
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading avatars from classpath: " + e.getMessage());
        }
        return avatars;
    }

    private List<String> getDefaultAvatars() {
        return List.of(
                "static/avatars/Avatar01.png",
                "static/avatars/Avatar03.png",
                "static/avatars/Avatar04.png"
        );
    }

    public String getRandomAvatar() {
        if (availableAvatars.isEmpty()) {
            return "static/avatars/Avatar01.png";
        }
        return availableAvatars.get(
                random.nextInt(availableAvatars.size())
        );
    }

    public List<String> getAvailableAvatars() {
        return new ArrayList<>(availableAvatars);
    }

    public int getAvailableAvatarsSize() {
        return availableAvatars.size();
    }*/
}
