package com.avrgaming.civcraft.object;

import org.bukkit.block.Biome;

public class Biomes {
	
	Biome biome;
	
	//XXX Hot
	private Biome getHotBiome() {
		if (biome.equals(Biome.DESERT) || biome.equals(Biome.DESERT_HILLS) ||
				biome.equals(Biome.SAVANNA) || biome.equals(Biome.SAVANNA_ROCK) ||
				biome.equals(Biome.MESA) || biome.equals(Biome.MESA_ROCK) ||
				biome.equals(Biome.MESA_CLEAR_ROCK) || biome.equals(Biome.HELL)) {
		}
		return biome;
	}
	
	@SuppressWarnings("unused")
	private Biome setHotBiome() {
		Biome biome = getHotBiome();
		return biome;
	}
	
	//XXX Warm
	private Biome getWarmBiome() {
		if (biome.equals(Biome.PLAINS) || biome.equals(Biome.FOREST) ||
				biome.equals(Biome.FOREST_HILLS) || biome.equals(Biome.JUNGLE) ||
				biome.equals(Biome.JUNGLE_HILLS) || biome.equals(Biome.JUNGLE_EDGE) ||
				biome.equals(Biome.MUSHROOM_ISLAND) || biome.equals(Biome.MUSHROOM_ISLAND_SHORE)) {
		}
		return biome;
	}
	
	@SuppressWarnings("unused")
	private Biome setWarmBiome() {
		Biome biome = getWarmBiome();
		return biome;
	}
	
	//XXX Neutral
	private Biome getNeutralBiome() {
		if (biome.equals(Biome.SKY) || biome.equals(Biome.VOID) ||
				biome.equals(Biome.BIRCH_FOREST) || biome.equals(Biome.BIRCH_FOREST_HILLS) ||
				biome.equals(Biome.ROOFED_FOREST) || biome.equals(Biome.BEACHES) ||
				biome.equals(Biome.STONE_BEACH) || biome.equals(Biome.COLD_BEACH)) {
		}
		return biome;
	}
	
	@SuppressWarnings("unused")
	private Biome setNeutralBiome() {
		Biome biome = getNeutralBiome();
		return biome;
	}
	
	//XXX Water
	private Biome getWaterBiome() {
		if (biome.equals(Biome.OCEAN) || biome.equals(Biome.DEEP_OCEAN) ||
				biome.equals(Biome.FROZEN_OCEAN) || biome.equals(Biome.FROZEN_RIVER) ||
				biome.equals(Biome.RIVER) || biome.equals(Biome.SWAMPLAND)) {
		}
		return biome;
	}
	
	@SuppressWarnings("unused")
	private Biome setWaterBiome() {
		Biome biome = getWaterBiome();
		return biome;
	}
	
	//XXX Cold
	private Biome getColdBiome() {
		if (biome.equals(Biome.EXTREME_HILLS) || biome.equals(Biome.SMALLER_EXTREME_HILLS) ||
				biome.equals(Biome.EXTREME_HILLS_WITH_TREES) || biome.equals(Biome.TAIGA) ||
				biome.equals(Biome.TAIGA_HILLS) || biome.equals(Biome.REDWOOD_TAIGA) ||
				biome.equals(Biome.REDWOOD_TAIGA_HILLS)) {
		}
		return biome;
	}
	
	@SuppressWarnings("unused")
	private Biome setColdBiome() {
		Biome biome = getColdBiome();
		return biome;
	}
	
	//XXX Snow
	private Biome getSnowBiome() {
		if (biome.equals(Biome.ICE_FLATS) || biome.equals(Biome.ICE_MOUNTAINS) ||
				biome.equals(Biome.TAIGA_COLD) || biome.equals(Biome.TAIGA_COLD_HILLS) ||
				biome.equals(Biome.RIVER)) {
		}
		return biome;
	}
	
	@SuppressWarnings("unused")
	private Biome setSnowBiome() {
		Biome biome = getSnowBiome();
		return biome;
	}
}
