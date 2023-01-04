package hamsteryds.nereusopus.enchants.artifact;

import hamsteryds.nereusopus.enchants.internal.enchants.entries.ArtifactEnchantment;
import org.bukkit.Color;
import org.bukkit.Particle;

import java.io.File;

public class ZapArtifactSpecialized extends ArtifactEnchantment {
    public ZapArtifactSpecialized(File file) {
        super(file);
    }

    @Override
    public Particle getParticle() {
        return Particle.REDSTONE;
    }

    @Override
    public Particle.DustOptions getOptions() {
        return new Particle.DustOptions(Color.YELLOW, 1.0f);
    }
}
