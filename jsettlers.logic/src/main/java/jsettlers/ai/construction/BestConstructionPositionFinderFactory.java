/*******************************************************************************
 * Copyright (c) 2016
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
package jsettlers.ai.construction;

import static jsettlers.common.buildings.EBuildingType.FARM;
import static jsettlers.common.buildings.EBuildingType.GOLD_MELT;
import static jsettlers.common.buildings.EBuildingType.GOLDMINE;
import static jsettlers.common.buildings.EBuildingType.IRON_MELT;
import static jsettlers.common.buildings.EBuildingType.IRON_MINE;
import static jsettlers.common.buildings.EBuildingType.LUMBERJACK;
import static jsettlers.common.buildings.EBuildingType.MILL;
import static jsettlers.common.buildings.EBuildingType.PIG_FARM;
import static jsettlers.common.buildings.EBuildingType.WEAPON_SMITH;
import static jsettlers.common.landscape.EResourceType.COAL;
import static jsettlers.common.landscape.EResourceType.GOLDORE;
import static jsettlers.common.landscape.EResourceType.IRONORE;
import jsettlers.common.buildings.EBuildingType;
import jsettlers.common.player.ECivilisation;
import jsettlers.common.position.ShortPoint2D;

import java.util.List;

public class BestConstructionPositionFinderFactory {

	public final IBestConstructionPositionFinder getBestConstructionPositionFinderFor(EBuildingType type, ECivilisation civilisation) {
		switch (type) {
		case STONE_CUTTER:
			return new BestStoneCutterConstructionPositionFinder(civilisation);
		case LUMBERJACK:
			return new BestLumberJackConstructionPositionFinder(type, civilisation);
		case FORESTER:
			return new BestForesterConstructionPositionFinder(type, civilisation);
		case SAWMILL:
			return new NearRequiredBuildingConstructionPositionFinder(type, LUMBERJACK);
		case TOWER:
		case BIG_TOWER:
		case CASTLE:
			return new BestMilitaryConstructionPositionFinder(type, civilisation);
		case FARM:
			return new BestFarmConstructionPositionFinder(civilisation);
		case WINEGROWER:
			return new BestWinegrowerConstructionPositionFinder();
		case COALMINE:
			return new BestMineConstructionPositionFinder(type, COAL, civilisation);
		case IRON_MINE:
			return new BestMineConstructionPositionFinder(type, IRONORE, civilisation);
		case GOLDMINE:
			return new BestMineConstructionPositionFinder(type, GOLDORE, civilisation);
		case WATERWORKS:
			return new BestWaterWorksConstructionPositionFinder(type);
		case IRON_MELT:
			return new NearRequiredBuildingConstructionPositionFinder(type, IRON_MINE);
		case WEAPON_SMITH:
			return new NearRequiredBuildingConstructionPositionFinder(type, IRON_MELT);
		case TOOL_SMITH:
			return new NearRequiredBuildingConstructionPositionFinder(type, IRON_MELT);
		case BARRACK:
			return new NearRequiredBuildingConstructionPositionFinder(type, WEAPON_SMITH);
		case MILL:
			return new NearRequiredBuildingConstructionPositionFinder(type, FARM);
		case BAKER:
			return new NearRequiredBuildingConstructionPositionFinder(type, MILL);
		case PIG_FARM:
			return new NearRequiredBuildingConstructionPositionFinder(type, FARM);
		case SLAUGHTERHOUSE:
			return new NearRequiredBuildingConstructionPositionFinder(type, PIG_FARM);
		case TEMPLE:
			return new BestTempleConstructionPositionFinder();
		case BIG_TEMPLE:
			return new BestBigTempleConstructionPositionFinder(civilisation);
		case GOLD_MELT:
			return new NearRequiredBuildingConstructionPositionFinder(type, GOLDMINE);
		case FISHER:
			return new BestFisherConstructionPositionFinder(type, civilisation);
		case STOCK:
			return new NearRequiredBuildingConstructionPositionFinder(type, GOLD_MELT);
		default:
			return new NearDiggersConstructionPositionFinder(type, civilisation);
		}
	}

	public final IBestConstructionPositionFinder getBorderDefenceConstructionPosition(List<ShortPoint2D> threatenedBorder) {
		return new BorderDefenceConstructionPositionFinder(threatenedBorder);
	}

}
