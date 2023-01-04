package hamsteryds.nereusopus.enchants.artifact;

import hamsteryds.nereusopus.enchants.internal.enchants.entries.ArtifactEnchantment;
import org.bukkit.Particle;

import java.io.File;

public class HeartArtifact extends ArtifactEnchantment {
    public HeartArtifact(File file) {
        super(file);
    }

    @Override
    public Particle getParticle() {
        return Particle.HEART;
    }
}
