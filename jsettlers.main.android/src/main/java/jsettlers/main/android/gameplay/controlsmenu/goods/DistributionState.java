package jsettlers.main.android.gameplay.controlsmenu.goods;

import jsettlers.common.buildings.EBuildingType;
import jsettlers.common.map.partition.IMaterialDistributionSettings;
import jsettlers.common.player.ECivilisation;

/**
 * Created by Tom Pratt on 29/09/2017.
 */

public class DistributionState {
	private final EBuildingType buildingType;
	private final ECivilisation civilisation;
	private final float ratio;

	public DistributionState(EBuildingType buildingType, ECivilisation civilisation, IMaterialDistributionSettings materialDistributionSettings) {
		this.buildingType = buildingType;
		this.civilisation = civilisation;
		this.ratio = materialDistributionSettings.getDistributionProbability(buildingType);
	}

	public EBuildingType getBuildingType() {
		return buildingType;
	}

	public float getRatio() {
		return ratio;
	}

	public ECivilisation getCivilisation() {
		return civilisation;
	}
}
