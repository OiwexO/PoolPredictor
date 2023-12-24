// Created by Denys on 14.07.2023.

#pragma once

namespace Offsets {

using OFFSET = unsigned int;

	namespace Ball {
		constexpr OFFSET Position       = 0x28;
		constexpr OFFSET Classification = 0x78;
		constexpr OFFSET State          = 0x7C;

//		constexpr OFFSET Spin           = 0x10;
//		constexpr OFFSET Velocity       = 0x38;
//		constexpr OFFSET Radius         = 0x50;
	}

	namespace Balls {
		constexpr OFFSET Count = 0x4;
		constexpr OFFSET Entry = 0xC;
		constexpr OFFSET Balls = 0x2F0;
	}

	namespace CueProperties {
		constexpr OFFSET CuePower    = 0x35685D0; //CUE_PROPERTIES_MAX_POWER
		constexpr OFFSET CueSpin     = 0x35685D8; //CUE_PROPERTIES_SPIN
		constexpr OFFSET CueAccuracy = 0x35685E0; //CUE_PROPERTIES_ACCURACY

		constexpr OFFSET VisualCue   = 0x2D8;
		constexpr OFFSET VisualGuide = 0x27C;
		constexpr OFFSET AimAngle    = 0x18;

		constexpr OFFSET Power       = 0x280;
		constexpr OFFSET SpinObject  = 0x2E0;
		constexpr OFFSET Spin        = 0x298;

	}

	namespace GameManager {
		constexpr OFFSET StateManager         = 0x300;
		constexpr OFFSET Rules                = 0x2A8;
		constexpr OFFSET GameMode             = 0x370;
		constexpr OFFSET Table                = 0x2AC;
		constexpr OFFSET PlayerClassification = 0x5C;

//		constexpr OFFSET MCMenuStateManager   = 0x2A0;
//		constexpr OFFSET MenuStateStack       = 0x8;
//		constexpr OFFSET MenuStateObjectCount = 0x4;
//		constexpr OFFSET PocketNominationMode = 0x38;
//		constexpr OFFSET NominatedPocket      = 0x94;
	}

	namespace SharedManager {
		constexpr OFFSET SharedGameManager  = 0x34E2238;
		constexpr OFFSET SharedMenuManager  = 0x350AA84;
		constexpr OFFSET SharedUserSettings = 0x35AFD50;

//		constexpr OFFSET SharedAdsManager   = 0x35DB9D4;
//		constexpr OFFSET SharedUserInfo     = 0x35AA79C;
	}

	namespace MenuManager {
		constexpr OFFSET MenuStateManager = 0x284;
	}

	namespace UserSettings {
		constexpr OFFSET WideGuideline = 0xE;
	}

    namespace CueStats {
        constexpr OFFSET cuePowerArray = 0x2D08E44;
        constexpr OFFSET cueSpinConstant = 0x35B9730;
		constexpr OFFSET setCuePropertiesMethod = 0x1C3D468;
    }

	/*
	namespace AdsManager {
		constexpr OFFSET SharedAdsManager        = 0x35DB9D4;
		constexpr OFFSET InterstitialsController = 0x84;
		constexpr OFFSET matchesWithoutAdOffset1 = 0xC;
		constexpr OFFSET matchesWithoutAdOffset2 = 0x20;
	}

	namespace TableInfo {
		constexpr OFFSET TablePockets           = 0x3F8;
		constexpr OFFSET TableCushions          = 0x3FC;
		constexpr OFFSET areTableCushionsActive = 0x400;

		//getPocketsPositions()
		constexpr OFFSET TableProperties = 0x284;
		constexpr OFFSET Pockets         = 0x34;

		//getTableShape()
		constexpr OFFSET TableShapeStart = 0x2F8;
	}
	*/

}
