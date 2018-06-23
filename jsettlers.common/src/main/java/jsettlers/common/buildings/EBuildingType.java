/*******************************************************************************
 * Copyright (c) 2015, 2016
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package jsettlers.common.buildings;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import jsettlers.common.buildings.jobs.IBuildingJob;
import jsettlers.common.buildings.loader.BuildingFile;
import jsettlers.common.buildings.stacks.ConstructionStack;
import jsettlers.common.buildings.stacks.RelativeStack;
import jsettlers.common.images.ImageLink;
import jsettlers.common.landscape.ELandscapeType;
import jsettlers.common.movable.EMovableType;
import jsettlers.common.player.ECivilisation;
import jsettlers.common.position.RelativePoint;

/**
 * This interface defines the main building type.
 * 
 * @author Michael Zangl
 * @author Andreas Eberle
 * @author Daniel Krause
 */
public enum EBuildingType {
	STONE_CUTTER,
	FORESTER,
	LUMBERJACK,
	SAWMILL,

	COALMINE,
	IRON_MINE,
	GOLDMINE,
	SULFUR_MINE(ECivilisation.ASIANS, ECivilisation.AMAZONS),
	GEM_MINE(ECivilisation.EGYPTIANS, ECivilisation.AMAZONS),
	GOLD_MELT,
	IRON_MELT,
	TOOL_SMITH,
	WEAPON_SMITH,

	FARM,
	PIG_FARM,
	/**
	 * Needs to implement {@link IBuilding.IMill}
	 */
	MILL,
	WATERWORKS,
	SLAUGHTERHOUSE,
	BAKER,
	FISHER,
	WINEGROWER(ECivilisation.ROMANS),
	CHARCOAL_BURNER(ECivilisation.ROMANS),
	DONKEY_FARM,
	BEEKEEPER(ECivilisation.AMAZONS),
	MEADMAKER(ECivilisation.AMAZONS),

	SMALL_LIVINGHOUSE,
	MEDIUM_LIVINGHOUSE,
	BIG_LIVINGHOUSE,

	LOOKOUT_TOWER,
	TOWER,
	BIG_TOWER,
	CASTLE,
	HOSPITAL,
	BARRACK,
	GONG_HALL(ECivilisation.AMAZONS),

	DOCKYARD,
	HARBOR,
	STOCK,

	TEMPLE,
	BIG_TEMPLE,
	ALCHEMIST(ECivilisation.AMAZONS),

	MARKET_PLACE;

	/**
	 * A copy of {@link #values()}. Do not modify this array. This is intended for quicker access to this value.
	 */
	public static final EBuildingType[] VALUES = EBuildingType.values();

	/**
	 * The number of buildings in the {@link #VALUES} array.
	 */
	public static final int NUMBER_OF_BUILDINGS = VALUES.length;
	public static final EnumSet<EBuildingType> MILITARY_BUILDINGS = EnumSet.of(TOWER, BIG_TOWER, CASTLE);

	/**
	 * The ordinal of this type. Yields more performance than using {@link #ordinal()}
	 */
	public final int ordinal;

	/**
	 * Stores all available building from each civilisation
	 */
	private final EnumMap<ECivilisation, BuildingInfo> buildingInfos;

	/**
	 * The required civilisations to build the type of building.
	 */
	public final List<ECivilisation> requiredCivilisations;

	/**
	 * Constructs an enum object.
	 */
	EBuildingType(ECivilisation... civilisations) {
		this.ordinal = ordinal();
		List civilisationsList = Arrays.asList(civilisations);
		this.requiredCivilisations = civilisationsList.isEmpty() ? Arrays.asList(ECivilisation.VALUES) : civilisationsList;

		buildingInfos = new EnumMap<>(ECivilisation.class);
		if (requiredCivilisations.contains(ECivilisation.ROMANS)) {
			buildingInfos.put(ECivilisation.ROMANS, new BuildingInfo(new BuildingFile(this.toString(), ECivilisation.ROMANS)));
		}
		if (requiredCivilisations.contains(ECivilisation.EGYPTIANS)) {
			buildingInfos.put(ECivilisation.EGYPTIANS, new BuildingInfo(new BuildingFile(this.toString(), ECivilisation.EGYPTIANS)));
		}
		if (requiredCivilisations.contains(ECivilisation.ASIANS)) {
			buildingInfos.put(ECivilisation.ASIANS, new BuildingInfo(new BuildingFile(this.toString(), ECivilisation.ASIANS)));
		}
		if (requiredCivilisations.contains(ECivilisation.AMAZONS)) {
			buildingInfos.put(ECivilisation.AMAZONS, new BuildingInfo(new BuildingFile(this.toString(), ECivilisation.AMAZONS)));

		}
	}

	private class BuildingInfo {
		private final IBuildingJob startJob;

		private final EMovableType workerType;

		private final RelativePoint doorTile;

		private final RelativePoint[] blockedTiles;

		private final short workRadius;

		private final boolean mine;

		private final ConstructionStack[] constructionStacks;

		private final RelativeStack[] requestStacks;

		private final RelativeStack[] offerStacks;

		private final RelativePoint workCenter;

		private final RelativePoint flag;

		private final RelativeBricklayer[] bricklayers;

		private final byte numberOfConstructionMaterials;

		private final ImageLink guiImage;

		private final ImageLink[] images;

		private final ImageLink[] buildImages;

		private final RelativePoint[] protectedTiles;

		private final RelativePoint[] buildMarks;

		private final EnumSet<ELandscapeType> groundTypes;

		private final short viewDistance;

		private final OccupierPlace[] occupierPlaces;

		private final BuildingAreaBitSet buildingAreaBitSet;

		BuildingInfo(BuildingFile buildingFile) {
			startJob = buildingFile.getStartJob();
			workerType = buildingFile.getWorkerType();
			doorTile = buildingFile.getDoor();
			blockedTiles = buildingFile.getBlockedTiles();
			protectedTiles = buildingFile.getProtectedTiles();

			constructionStacks = buildingFile.getConstructionRequiredStacks();
			requestStacks = buildingFile.getRequestStacks();
			offerStacks = buildingFile.getOfferStacks();

			workRadius = buildingFile.getWorkRadius();
			workCenter = buildingFile.getWorkCenter();
			mine = buildingFile.isMine();
			flag = buildingFile.getFlag();
			bricklayers = buildingFile.getBricklayers();
			occupierPlaces = buildingFile.getOccupierPlaces();
			guiImage = buildingFile.getGuiImage();

			images = buildingFile.getImages();
			buildImages = buildingFile.getBuildImages();

			buildMarks = buildingFile.getBuildMarks();
			groundTypes = EnumSet.copyOf(buildingFile.getGroundTypes());
			viewDistance = buildingFile.getViewDistance();

			this.numberOfConstructionMaterials = calculateNumberOfConstructionMaterials();

			this.buildingAreaBitSet = new BuildingAreaBitSet(protectedTiles);

			if (mine) {
				this.buildingAreaBitSet.setCenter((short) 1, (short) 1);
			}
		}

		private byte calculateNumberOfConstructionMaterials() {
			byte sum = 0;
			for (ConstructionStack stack : constructionStacks) {
				sum += stack.requiredForBuild();
			}
			return sum;
		}
	}

	public RelativePoint[] getBuildingArea(ECivilisation civilisation) {
		return buildingInfos.get(civilisation).protectedTiles;
	}

	/**
	 * Gets the job a worker for this building should start with.
	 *
	 * @param civilisation
	 *            The civilisation of the building.
	 * @return That {@link IBuildingJob}
	 */
	public final IBuildingJob getStartJob(ECivilisation civilisation) {
		return buildingInfos.get(civilisation).startJob;
	}

	/**
	 * Gets the type of worker required for the building.
	 *
	 * @param civilisation
	 *            The civilisation of the building.
	 * @return The worker or <code>null</code> if no worker is required.
	 */
	public final EMovableType getWorkerType(ECivilisation civilisation) {
		return buildingInfos.get(civilisation).workerType;
	}

	/**
	 * Gets the position of the door for this building.
	 *
	 * @param civilisation
	 *            The civilisation of the building.
	 * @return The door.
	 */
	public final RelativePoint getDoorTile(ECivilisation civilisation) {
		return buildingInfos.get(civilisation).doorTile;
	}

	/**
	 * Gets a list of blocked positions.
	 *
	 * @param civilisation
	 *            The civilisation of the building.
	 * @return The list of blocked positions.
	 */
	public final RelativePoint[] getBlockedTiles(ECivilisation civilisation) {
		return buildingInfos.get(civilisation).blockedTiles;
	}

	/**
	 * Gets the tiles that are protected by this building. On this tiles, no other buildings may be build.
	 *
	 * @param civilisation
	 *            The civilisation of the building.
	 * @return The tiles as array.
	 */
	public final RelativePoint[] getProtectedTiles(ECivilisation civilisation) {
		return buildingInfos.get(civilisation).protectedTiles;
	}

	/**
	 * Gets the images needed to display this building. They are rendered in the order provided.
	 *
	 * @param civilisation
	 *            The civilisation of the building.
	 * @return The images
	 */
	public final ImageLink[] getImages(ECivilisation civilisation) {
		return buildingInfos.get(civilisation).images;
	}

	/**
	 * Gets the images needed to display this building while it si build. They are rendered in the order provided.
	 *
	 * @param civilisation
	 *            The civilisation of the building.
	 * @return The images
	 */
	public final ImageLink[] getBuildImages(ECivilisation civilisation) {
		return buildingInfos.get(civilisation).buildImages;
	}

	/**
	 * Gets the gui image that is displayed in the building selection dialog.
	 *
	 * @param civilisation
	 *            The civilisation of the building.
	 * @return The image. It may be <code>null</code>
	 */
	public final ImageLink getGuiImage(ECivilisation civilisation) {
		return buildingInfos.get(civilisation).guiImage;
	}

	/**
	 * Gets the working radius of the building. If it is 0, the building does not support a working radius.
	 *
	 * @param civilisation
	 *            The civilisation of the building.
	 * @return The radius.
	 */
	public final short getWorkRadius(ECivilisation civilisation) {
		return buildingInfos.get(civilisation).workRadius;
	}

	/**
	 * Gets the default work center for the building type.
	 *
	 * @param civilisation
	 *            The civilisation of the building.
	 * @return The default work center position.
	 */
	public final RelativePoint getDefaultWorkcenter(ECivilisation civilisation) {
		return buildingInfos.get(civilisation).workCenter;
	}

	/**
	 * Gets the position of the flag for this building. The flag type is determined by the building itself.
	 *
	 * @param civilisation
	 *            The civilisation of the building.
	 * @return The flag position.
	 */
	public final RelativePoint getFlag(ECivilisation civilisation) {
		return buildingInfos.get(civilisation).flag;
	}

	/**
	 * Gets the positions where the bricklayers should stand to build the house.
	 *
	 * @param civilisation
	 *            The civilisation of the building.
	 * @return The positions.
	 * @see RelativeBricklayer
	 */
	public final RelativeBricklayer[] getBricklayers(ECivilisation civilisation) {
		return buildingInfos.get(civilisation).bricklayers;
	}

	/**
	 * Gets the positions of the build marks (sticks) for this building.
	 *
	 * @param civilisation
	 *            The civilisation of the building.
	 * @return The positions of the marks.
	 */
	public final RelativePoint[] getBuildMarks(ECivilisation civilisation) {
		return buildingInfos.get(civilisation).buildMarks;
	}

	/**
	 * Gets the ground types this building can be placed on.
	 *
	 * @param civilisation
	 *            The civilisation of the building.
	 * @return The ground types.
	 */
	public final Set<ELandscapeType> getGroundTypes(ECivilisation civilisation) {
		return buildingInfos.get(civilisation).groundTypes;
	}

	/**
	 * Gets the distance the FOW should be set to visible around this building.
	 *
	 * @param civilisation
	 *            The civilisation of the building.
	 * @return The view distance.
	 */
	public final short getViewDistance(ECivilisation civilisation) {
		return buildingInfos.get(civilisation).viewDistance;
	}

	/**
	 * Gets the places where occupiers can be in this building.
	 *
	 * @return The places.
	 * @see OccupierPlace
	 */
	public final OccupierPlace[] getOccupierPlaces(ECivilisation civilisation) {
		return buildingInfos.get(civilisation).occupierPlaces;
	}

	/**
	 * Queries a building job with the given name that needs to be accessible from the start job.
	 *
	 * @param jobname
	 *            The name of the job.
	 * @param civilisation
	 *            The civilisation of the building.
	 * @return The job if found.
	 * @throws IllegalArgumentException
	 *             If the name was not found.
	 */
	public final IBuildingJob getJobByName(String jobname, ECivilisation civilisation) {
		HashSet<String> visited = new HashSet<>();

		ConcurrentLinkedQueue<IBuildingJob> queue = new ConcurrentLinkedQueue<>();
		queue.add(buildingInfos.get(civilisation).startJob);

		while (!queue.isEmpty()) {
			IBuildingJob job = queue.poll();
			if (visited.contains(job.getName())) {
				continue;
			}
			if (job.getName().equals(jobname)) {
				return job;
			}
			visited.add(job.getName());

			queue.add(job.getNextFailJob());
			queue.add(job.getNextSucessJob());
		}
		throw new IllegalArgumentException("This building has no job with name " + jobname);
	}

	/**
	 * Gets the area for this building.
	 *
	 * @param civilisation
	 *            The civilisation of the building.
	 * @return The building area.
	 */
	public final BuildingAreaBitSet getBuildingAreaBitSet(ECivilisation civilisation) {
		return buildingInfos.get(civilisation).buildingAreaBitSet;
	}

	/**
	 * Gets the materials required to build this building and where to place them.
	 *
	 * @param civilisation
	 *            The civilisation of the building.
	 * @return The array of material stacks.
	 */
	public ConstructionStack[] getConstructionStacks(ECivilisation civilisation) {
		return buildingInfos.get(civilisation).constructionStacks;
	}

	/**
	 * Get the amount of material required to build this house. Usually the number of stone + planks.
	 *
	 * @param civilisation
	 *            The civilisation of the building.
	 * @return The number of materials required to construct the building.
	 */
	public final byte getNumberOfConstructionMaterials(ECivilisation civilisation) {
		return buildingInfos.get(civilisation).numberOfConstructionMaterials;
	}

	/**
	 * Gets the request stacks required to operate this building.
	 *
	 * @param civilisation
	 *            The civilisation of the building.
	 * @return The request stacks.
	 */
	public RelativeStack[] getRequestStacks(ECivilisation civilisation) {
		return buildingInfos.get(civilisation).requestStacks;
	}

	/**
	 * Gets the request stacks required to operate this building type (civilisation independent).
	 *
	 * @return The request stacks.
	 */
	public RelativeStack[] getRequestStacks() {
		for (ECivilisation civilisation : ECivilisation.VALUES) {
			BuildingInfo file = buildingInfos.get(civilisation);
			if (file != null)
				return file.requestStacks;
		}
		// should never happen
		throw new AssertionError();
	}

	/**
	 * Gets the positions where the building should offer materials.
	 *
	 * @param civilisation
	 *            The civilisation of the building.
	 * @return The offer positions.
	 */
	public RelativeStack[] getOfferStacks(ECivilisation civilisation) {
		return buildingInfos.get(civilisation).offerStacks;
	}

	/**
	 * Checks if this building is a mine.
	 *
	 * @return <code>true</code> iff this building is a mine.
	 */
	public boolean isMine() {
		if(requiredCivilisations.isEmpty())
			return false;
		else
			return buildingInfos.get(requiredCivilisations.get(0)).mine;
	}

	public boolean needsFlattenedGround(ECivilisation civilisation) {
		return !buildingInfos.get(civilisation).mine;
	}

	/**
	 * Checks if this building is a military building.
	 *
	 * @return <code>true</code> iff this is a military building.
	 */
	public boolean isMilitaryBuilding() {
		return MILITARY_BUILDINGS.contains(this);
	}

	public Set<ELandscapeType> getRequiredGroundTypeAt(int relativeX, int relativeY, ECivilisation civilisation) {
		if (relativeX == 0 && relativeY == 0 && buildingInfos.get(civilisation).mine) { // if it is a mine and we are in the center
			return ELandscapeType.MOUNTAIN_TYPES;
		} else {
			return buildingInfos.get(civilisation).groundTypes;
		}
	}
}
