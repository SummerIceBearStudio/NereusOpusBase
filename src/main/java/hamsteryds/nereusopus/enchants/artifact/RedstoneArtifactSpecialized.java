package hamsteryds.nereusopus.enchants.artifact;

import hamsteryds.nereusopus.enchants.internal.enchants.entries.ArtifactEnchantment;
import org.bukkit.Color;
import org.bukkit.Particle;

import java.io.File;

public class RedstoneArtifactSpecialized extends ArtifactEnchantment {
    public RedstoneArtifactSpecialized(File file) {
        super(file);
    }

    @Override
    public Particle getParticle() {
        return Particle.REDSTONE;
    }

    @Override
    public Particle.DustOptions getOptions() {
        return new Particle.DustOptions(Color.RED, 1.0f);
    }
}
