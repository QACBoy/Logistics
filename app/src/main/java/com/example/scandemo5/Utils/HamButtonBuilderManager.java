package com.example.scandemo5.Utils;

import android.graphics.Color;
import android.util.Pair;

import com.example.scandemo5.R;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.nightonke.boommenu.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sam on 2017/9/20.
 */

public class HamButtonBuilderManager {

    private static int[] imageResources = new int[]{
            R.drawable.bat,
            R.drawable.bear,
            R.drawable.bee,
            R.drawable.butterfly,
            R.drawable.cat,
            R.drawable.deer,
            R.drawable.dolphin,
            R.drawable.eagle,
            R.drawable.horse,
            R.drawable.elephant,
            R.drawable.owl,
            R.drawable.peacock,
            R.drawable.pig,
            R.drawable.rat,
            R.drawable.snake,
            R.drawable.squirrel
    };

    private static int imageResourceIndex = 0;

    static int getImageResource() {
        if (imageResourceIndex >= imageResources.length) imageResourceIndex = 0;
        return imageResources[imageResourceIndex++];
    }

    static SimpleCircleButton.Builder getSimpleCircleButtonBuilder() {
        return new SimpleCircleButton.Builder()
                .normalImageRes(getImageResource());
    }

    static SimpleCircleButton.Builder getSquareSimpleCircleButtonBuilder() {
        return new SimpleCircleButton.Builder()
                .isRound(false)
                .shadowCornerRadius(Util.dp2px(20))
                .buttonCornerRadius(Util.dp2px(20))
                .normalImageRes(getImageResource());
    }
/*
    static TextInsideCircleButton.Builder getTextInsideCircleButtonBuilder() {
        return new TextInsideCircleButton.Builder()
                .normalImageRes(getImageResource())
                .normalTextRes(R.string.text_inside_circle_button_text_normal);
    }*/
/*
    static TextInsideCircleButton.Builder getSquareTextInsideCircleButtonBuilder() {
        return new TextInsideCircleButton.Builder()
                .isRound(false)
                .shadowCornerRadius(Util.dp2px(10))
                .buttonCornerRadius(Util.dp2px(10))
                .normalImageRes(getImageResource())
                .normalTextRes(R.string.text_inside_circle_button_text_normal);
    }*/

/*    static TextInsideCircleButton.Builder getTextInsideCircleButtonBuilderWithDifferentPieceColor() {
        return new TextInsideCircleButton.Builder()
                .normalImageRes(getImageResource())
                .normalTextRes(R.string.text_inside_circle_button_text_normal)
                .pieceColor(Color.WHITE);
    }*/
/*
    static TextOutsideCircleButton.Builder getTextOutsideCircleButtonBuilder() {
        return new TextOutsideCircleButton.Builder()
                .normalImageRes(getImageResource())
                .normalTextRes(R.string.text_outside_circle_button_text_normal);
    }*/

/*    static TextOutsideCircleButton.Builder getSquareTextOutsideCircleButtonBuilder() {
        return new TextOutsideCircleButton.Builder()
                .isRound(false)
                .shadowCornerRadius(Util.dp2px(15))
                .buttonCornerRadius(Util.dp2px(15))
                .normalImageRes(getImageResource())
                .normalTextRes(R.string.text_outside_circle_button_text_normal);
    }*/

    public static TextOutsideCircleButton.Builder getTextOutsideCircleButtonBuilderWithDifferentPieceColor(int id) {
        int [] textId={ R.string.text_outside_circle_button_text_0,
                        R.string.text_outside_circle_button_text_1,
                        R.string.text_outside_circle_button_text_2,
                        R.string.text_outside_circle_button_text_3,
                        R.string.text_outside_circle_button_text_4,
                        R.string.text_outside_circle_button_text_5,
                        R.string.text_outside_circle_button_text_6,
                        R.string.text_outside_circle_button_text_7,
                        R.string.text_outside_circle_button_text_8
        };
        return new TextOutsideCircleButton.Builder()
                .normalImageRes(getImageResource())
                .normalTextRes(textId[id])
                .pieceColor(Color.WHITE);
    }
/*

    static HamButton.Builder getHamButtonBuilder() {
        return new HamButton.Builder()
                .normalImageRes(getImageResource())
                .normalTextRes(R.string.text_ham_button_text_normal)
                .subNormalTextRes(R.string.text_ham_button_sub_text_normal);
    }
*/

    static HamButton.Builder getHamButtonBuilder(String text, String subText) {
        return new HamButton.Builder()
                .normalImageRes(getImageResource())
                .normalText(text)
                .subNormalText(subText);
    }

/*    static HamButton.Builder getPieceCornerRadiusHamButtonBuilder() {
        return new HamButton.Builder()
                .normalImageRes(getImageResource())
                .normalTextRes(R.string.text_ham_button_text_normal)
                .subNormalTextRes(R.string.text_ham_button_sub_text_normal);
    }*/

    private static int[] righttextId; //右菜单文字
    public static int[] setTextId = {
            R.string.ham_button_set,
    };
    public static int[] mainTextId = {
                R.string.main_ham_button_text_0,
                R.string.main_ham_button_text_1,
                R.string.main_ham_button_text_2,
                R.string.ham_button_set,
    };
    public static int[] changestoragetextId = {
            R.string.changestorage_ham_button_text_0,
            R.string.changestorage_ham_button_text_1,
            R.string.ham_button_set,
    };
    public static int[] outgoingdetailtextId = {
            R.string.outgoing_ham_button_text_0,
            R.string.outgoing_ham_button_text_1,
            R.string.ham_button_set,
    };
    public static int[] distristardetailtextId = {
            R.string.distrbutiondetial_ham_button_text_0,
            R.string.ham_button_set,
    };
    public static int[] outgoingsureetailtextId = {
            R.string.outgoingsuredetail_ham_button_text_0,
            R.string.ham_button_set,
    };
    public static int[] distristartextId = {
            R.string.distrbutiondetial_ham_button_text_1,
            R.string.ham_button_set,
    };
    public static int[] distristaroutgoingtextId = {
            R.string.distrbutiondetial_ham_button_text_2,
            R.string.ham_button_set,
    };
    public static void setHamButtonText(int[] textId){
        righttextId = textId;
    }
    public static int[] getHamButtonText(){
        return righttextId;
    }

    public static HamButton.Builder getHamButtonBuilderWithDifferentPieceColor(int id) {
        return new HamButton.Builder()
                .normalImageRes(getImageResource())
                .normalTextRes(righttextId[id])
//                .subNormalTextRes(R.string.text_ham_button_sub_text_normal)
                .pieceColor(Color.WHITE);
    }

    static List<String> getCircleButtonData(ArrayList<Pair> piecesAndButtons) {
        List<String> data = new ArrayList<>();
        for (int p = 0; p < PiecePlaceEnum.values().length - 1; p++) {
            for (int b = 0; b < ButtonPlaceEnum.values().length - 1; b++) {
                PiecePlaceEnum piecePlaceEnum = PiecePlaceEnum.getEnum(p);
                ButtonPlaceEnum buttonPlaceEnum = ButtonPlaceEnum.getEnum(b);
                if (piecePlaceEnum.pieceNumber() == buttonPlaceEnum.buttonNumber()
                        || buttonPlaceEnum == ButtonPlaceEnum.Horizontal
                        || buttonPlaceEnum == ButtonPlaceEnum.Vertical) {
                    piecesAndButtons.add(new Pair<>(piecePlaceEnum, buttonPlaceEnum));
                    data.add(piecePlaceEnum + " " + buttonPlaceEnum);
                    if (piecePlaceEnum == PiecePlaceEnum.HAM_1
                            || piecePlaceEnum == PiecePlaceEnum.HAM_2
                            || piecePlaceEnum == PiecePlaceEnum.HAM_3
                            || piecePlaceEnum == PiecePlaceEnum.HAM_4
                            || piecePlaceEnum == PiecePlaceEnum.HAM_5
                            || piecePlaceEnum == PiecePlaceEnum.HAM_6
                            || piecePlaceEnum == PiecePlaceEnum.Share
                            || piecePlaceEnum == PiecePlaceEnum.Custom
                            || buttonPlaceEnum == ButtonPlaceEnum.HAM_1
                            || buttonPlaceEnum == ButtonPlaceEnum.HAM_2
                            || buttonPlaceEnum == ButtonPlaceEnum.HAM_3
                            || buttonPlaceEnum == ButtonPlaceEnum.HAM_4
                            || buttonPlaceEnum == ButtonPlaceEnum.HAM_5
                            || buttonPlaceEnum == ButtonPlaceEnum.HAM_6
                            || buttonPlaceEnum == ButtonPlaceEnum.Custom) {
                        piecesAndButtons.remove(piecesAndButtons.size() - 1);
                        data.remove(data.size() - 1);
                    }
                }
            }
        }
        return data;
    }

    static List<String> getHamButtonData(ArrayList<Pair> piecesAndButtons) {
        List<String> data = new ArrayList<>();
        for (int p = 0; p < PiecePlaceEnum.values().length - 1; p++) {
            for (int b = 0; b < ButtonPlaceEnum.values().length - 1; b++) {
                PiecePlaceEnum piecePlaceEnum = PiecePlaceEnum.getEnum(p);
                ButtonPlaceEnum buttonPlaceEnum = ButtonPlaceEnum.getEnum(b);
                if (piecePlaceEnum.pieceNumber() == buttonPlaceEnum.buttonNumber()
                        || buttonPlaceEnum == ButtonPlaceEnum.Horizontal
                        || buttonPlaceEnum == ButtonPlaceEnum.Vertical) {
                    piecesAndButtons.add(new Pair<>(piecePlaceEnum, buttonPlaceEnum));
                    data.add(piecePlaceEnum + " " + buttonPlaceEnum);
                    if (piecePlaceEnum.getValue() < PiecePlaceEnum.HAM_1.getValue()
                            || piecePlaceEnum == PiecePlaceEnum.Share
                            || piecePlaceEnum == PiecePlaceEnum.Custom
                            || buttonPlaceEnum.getValue() < ButtonPlaceEnum.HAM_1.getValue()) {
                        piecesAndButtons.remove(piecesAndButtons.size() - 1);
                        data.remove(data.size() - 1);
                    }
                }
            }
        }
        return data;
    }

    private static HamButtonBuilderManager ourInstance = new HamButtonBuilderManager();

    public static HamButtonBuilderManager getInstance() {
        return ourInstance;
    }

    private HamButtonBuilderManager() {
    }
}