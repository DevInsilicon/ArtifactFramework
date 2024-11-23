package dev.insilicon.artifactFramework.BaseInternal.InternalLibs;

import dev.insilicon.artifactFramework.ArtifactFramework;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class ParticleHelper {

    public static void wave(Location position, Particle particle, int resolution, int tickAge, int speed) {
        World world = position.getWorld();
        if (world == null) return;

        double radius = 1.0;
        for (int i = 0; i < resolution; i++) {
            double angle = 2 * Math.PI * i / resolution;
            double x = position.getX() + radius * Math.cos(angle);
            double z = position.getZ() + radius * Math.sin(angle);
            Location particleLocation = new Location(world, x, position.getY(), z);
            world.spawnParticle(particle, particleLocation, 1, 0, 0, 0, speed);
            radius += 0.1;
        }
    }

    public static void circle(Location position, Particle particle, int resolution, double radius, int speed) {
        World world = position.getWorld();
        if (world == null) return;

        for (int i = 0; i < resolution; i++) {
            double angle = 2 * Math.PI * i / resolution;
            double x = position.getX() + radius * Math.cos(angle);
            double z = position.getZ() + radius * Math.sin(angle);
            Location particleLocation = new Location(world, x, position.getY(), z);
            world.spawnParticle(particle, particleLocation, 1, 0, 0, 0, speed);
        }
    }

    public static void sphere(Location position, Particle particle, int resolution, double radius, int speed) {
        World world = position.getWorld();
        if (world == null) return;

        for (int i = 0; i < resolution; i++) {
            double theta = Math.acos(2 * Math.random() - 1);
            double phi = 2 * Math.PI * Math.random();
            double x = position.getX() + radius * Math.sin(theta) * Math.cos(phi);
            double y = position.getY() + radius * Math.sin(theta) * Math.sin(phi);
            double z = position.getZ() + radius * Math.cos(theta);
            Location particleLocation = new Location(world, x, y, z);
            world.spawnParticle(particle, particleLocation, 1, 0, 0, 0, speed);
        }
    }

    public static void spiral(Location position, Particle particle, int resolution, double radius, double height, int speed) {
        World world = position.getWorld();
        if (world == null) return;

        for (int i = 0; i < resolution; i++) {
            double angle = 2 * Math.PI * i / resolution;
            double x = position.getX() + radius * Math.cos(angle);
            double y = position.getY() + (height * i / resolution);
            double z = position.getZ() + radius * Math.sin(angle);
            Location particleLocation = new Location(world, x, y, z);
            world.spawnParticle(particle, particleLocation, 1, 0, 0, 0, speed);
        }
    }

    public static void line(Location start, Location end, Particle particle, int resolution, int speed) {
        World world = start.getWorld();
        if (world == null || !world.equals(end.getWorld())) return;

        double distance = start.distance(end);
        for (int i = 0; i < resolution; i++) {
            double t = (double) i / (resolution - 1);
            double x = start.getX() + t * (end.getX() - start.getX());
            double y = start.getY() + t * (end.getY() - start.getY());
            double z = start.getZ() + t * (end.getZ() - start.getZ());
            Location particleLocation = new Location(world, x, y, z);
            world.spawnParticle(particle, particleLocation, 1, 0, 0, 0, speed);
        }
    }

    public static void square(Location position, Particle outlineParticle, Particle shadeParticle, int size, boolean doShade, int rotation) {
        World world = position.getWorld();
        if (world == null) return;

        double angle = Math.toRadians(rotation);
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        for (int x = -size; x <= size; x++) {
            for (int z = -size; z <= size; z++) {
                double rotatedX = x * cos - z * sin;
                double rotatedZ = x * sin + z * cos;
                Location particleLocation = position.clone().add(rotatedX, 0, rotatedZ);

                if (x == -size || x == size || z == -size || z == size) {
                    world.spawnParticle(outlineParticle, particleLocation, 1, 0, 0, 0, 0);
                } else if (doShade) {
                    world.spawnParticle(shadeParticle, particleLocation, 1, 0, 0, 0, 0);
                }
            }
        }
    }

    public static void cylinder(Location position, Particle particle, int radius, int height, int resolution) {
        World world = position.getWorld();
        if (world == null) return;

        for (int y = 0; y <= height; y++) {
            for (int i = 0; i < resolution; i++) {
                double angle = 2 * Math.PI * i / resolution;
                double x = position.getX() + radius * Math.cos(angle);
                double z = position.getZ() + radius * Math.sin(angle);
                Location particleLocation = new Location(world, x, position.getY() + y, z);
                world.spawnParticle(particle, particleLocation, 1, 0, 0, 0, 0);
            }
        }
    }

    public static void plane(Location position, Particle particle, int width, int length, int rotation) {
        World world = position.getWorld();
        if (world == null) return;

        double angle = Math.toRadians(rotation);
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        double halfWidth = width / 2.0;
        double halfLength = length / 2.0;

        for (int x = -width / 2; x <= width / 2; x++) {
            for (int z = -length / 2; z <= length / 2; z++) {
                double rotatedX = x * cos - z * sin;
                double rotatedZ = x * sin + z * cos;
                Location particleLocation = position.clone().add(rotatedX, 0, rotatedZ);
                world.spawnParticle(particle, particleLocation, 4, 0, 0, 0, 0);
            }
        }
    }

    public static void verticalPlane(Location position, Particle particle, int width, int height, int resolution, int rotation) {
        World world = position.getWorld();
        if (world == null) return;

        double angle = Math.toRadians(rotation);
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        for (int x = 0; x <= width; x++) {
            for (int y = 0; y <= height; y++) {
                double rotatedX = x * cos - y * sin;
                double rotatedY = x * sin + y * cos;
                Location particleLocation = position.clone().add(rotatedX, rotatedY, 0);
                world.spawnParticle(particle, particleLocation, 1, 0, 0, 0, 0);
            }
        }
    }

    public static void sphere(Location position, Particle particle, int radius, int resolution) {
        World world = position.getWorld();
        if (world == null) return;

        for (int i = 0; i < resolution; i++) {
            double theta = Math.acos(2 * Math.random() - 1);
            double phi = 2 * Math.PI * Math.random();
            double x = position.getX() + radius * Math.sin(theta) * Math.cos(phi);
            double y = position.getY() + radius * Math.sin(theta) * Math.sin(phi);
            double z = position.getZ() + radius * Math.cos(theta);
            Location particleLocation = new Location(world, x, y, z);
            world.spawnParticle(particle, particleLocation, 1, 0, 0, 0, 0);
        }
    }

    public static void pyramid(Location position, Particle particle, int baseSize, int height, int resolution) {
        World world = position.getWorld();
        if (world == null) return;

        for (int y = 0; y <= height; y++) {
            int currentSize = baseSize - (baseSize * y / height);
            for (int x = -currentSize; x <= currentSize; x++) {
                for (int z = -currentSize; z <= currentSize; z++) {
                    Location particleLocation = position.clone().add(x, y, z);
                    world.spawnParticle(particle, particleLocation, 1, 0, 0, 0, 0);
                }
            }
        }
    }

    public static void waterWave(Location position, Particle particle, int width, int length, int height, int thickness, int resolution) {
        World world = position.getWorld();
        if (world == null) return;

        for (int x = 0; x <= width; x++) {
            for (int z = 0; z <= length; z++) {
                double waveHeight = height * Math.sin((double) z / length * 2 * Math.PI);
                for (int y = 0; y <= waveHeight; y++) {
                    for (int t = 0; t <= thickness; t++) {
                        Location particleLocation = position.clone().add(x, y, z + t);
                        world.spawnParticle(particle, particleLocation, 1, 0, 0, 0, 0);
                    }
                }
            }
        }
    }

    public static void expandingSphere(Location position, Particle particle, double initialRadius, double expansionRate, int resolution, int duration, int speed) {
        World world = position.getWorld();
        if (world == null) return;

        new BukkitRunnable() {
            double currentRadius = initialRadius;
            int ticks = 0;

            @Override
            public void run() {
                if (ticks >= duration) {
                    this.cancel();
                    return;
                }

                for (int i = 0; i < resolution; i++) {
                    double theta = Math.acos(2 * Math.random() - 1);
                    double phi = 2 * Math.PI * Math.random();
                    double x = position.getX() + currentRadius * Math.sin(theta) * Math.cos(phi);
                    double y = position.getY() + currentRadius * Math.sin(theta) * Math.sin(phi);
                    double z = position.getZ() + currentRadius * Math.cos(theta);
                    Location particleLocation = new Location(world, x, y, z);
                    world.spawnParticle(particle, particleLocation, 1, 0, 0, 0, speed);
                }

                currentRadius += expansionRate;
                ticks++;
            }
        }.runTaskTimer(ArtifactFramework.getInstance(), 0, 1);
    }

    public static void expandingWave(Location position, Particle particle, double initialRadius, double expansionRate, int resolution, int duration, int thickness) {
        World world = position.getWorld();
        if (world == null) return;

        new BukkitRunnable() {
            double currentRadius = initialRadius;
            int ticks = 0;
            double speed = expansionRate / duration;

            @Override
            public void run() {
                if (ticks >= duration) {
                    this.cancel();
                    return;
                }

                for (int i = 0; i < resolution; i++) {
                    double angle = 2 * Math.PI * i / resolution;
                    for (int t = 0; t < thickness; t++) {
                        double x = position.getX() + (currentRadius + t) * Math.cos(angle);
                        double z = position.getZ() + (currentRadius + t) * Math.sin(angle);
                        Location particleLocation = new Location(world, x, position.getY(), z);
                        world.spawnParticle(particle, particleLocation, 1, 0, 0, 0, speed);
                    }
                }

                currentRadius += expansionRate;
                ticks++;
            }
        }.runTaskTimer(ArtifactFramework.getInstance(), 0, 1);
    }

}