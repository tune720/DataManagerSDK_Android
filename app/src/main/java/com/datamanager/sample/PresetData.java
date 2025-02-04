package com.datamanager.sample;

import android.os.Bundle;

import com.enliple.datamanagersdk.ENDataManager;
import com.enliple.datamanagersdk.events.models.ENProduct;

import java.util.ArrayList;
import java.util.HashMap;

public class PresetData {

    public static String nTtileNameKey = "list_title";
    public static String WebViewTestTitle = "WebView Test";

    public static Bundle makeWebViewData() {
        Bundle bundle = new Bundle();
        bundle.putString(nTtileNameKey, WebViewTestTitle);

        return bundle;
    }




    private static Bundle makeDefaultData(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(ENDataManager.Params.type, type);

        return bundle;
    }

    private static Bundle addProductList() {
        Bundle bundle = new Bundle();
        bundle.putString(ENDataManager.Params.productId, "product_id");
        bundle.putString(ENDataManager.Params.productName, "product_name");
        bundle.putInt(ENDataManager.Params.productPrice, 10000);
        bundle.putString(ENDataManager.Params.productImageUrl, "product_image_url");
        bundle.putString(ENDataManager.Params.productUrl, "product_url");
        bundle.putInt(ENDataManager.Params.productQty, 1);

        return bundle;
    }


    public static Bundle makeProductData() {
        return addProductList();
    }

    public static Bundle makeCustomData() {
        Bundle bundle = makeDefaultData(ENDataManager.EventType.custom);
        bundle.putString(nTtileNameKey, "커스텀 이벤트");
        bundle.putString(ENDataManager.Params.id, "custom_event_id");
        bundle.putString(ENDataManager.Params.eventName, "마이페이지 이탈");

        return bundle;
    }


    public static Bundle makeCartData() {
        Bundle bundle = makeDefaultData(ENDataManager.EventType.cart);
        bundle.putString(nTtileNameKey, "장바구니");
        bundle.putAll(addProductList());

        return bundle;
    }

    public static Bundle makeFavoriteData() {
        Bundle bundle = makeDefaultData(ENDataManager.EventType.favorite);
        bundle.putString(nTtileNameKey, "찜 상품");
        bundle.putAll(addProductList());

        return bundle;
    }

    public static Bundle makeOrderData() {
        Bundle bundle = makeDefaultData(ENDataManager.EventType.order);
        bundle.putString(nTtileNameKey, "주문");
        bundle.putString(ENDataManager.Params.products, "");
        bundle.putString(ENDataManager.Params.orderId, "order_id");
        bundle.putString(ENDataManager.Params.zipCode, "zip_code");
        bundle.putString(ENDataManager.Params.phoneNumber, "phone_number");
        bundle.putString(ENDataManager.Params.address, "address");
        bundle.putInt(ENDataManager.Params.totalPrice, 10000);
        bundle.putInt(ENDataManager.Params.totalQuantity, 5);
        bundle.putString(ENDataManager.Params.paymentMethod, "payment_method");
        bundle.putString(ENDataManager.Params.memberName, "member_name");
        bundle.putString(ENDataManager.Params.email, "email");
        bundle.putString(ENDataManager.Params.memberId, "member_id");

        return bundle;
    }

    public static Bundle makeOrderCancelData() {
        Bundle bundle = makeDefaultData(ENDataManager.EventType.orderCancel);
        bundle.putString(nTtileNameKey, "주문취소");
        bundle.putString(ENDataManager.Params.products, "");
        bundle.putString(ENDataManager.Params.memberId, "member_id");
        bundle.putString(ENDataManager.Params.orderId, "order_id");

        return bundle;
    }

    // OrderOut
    public static Bundle makeOrderOutData() {
        Bundle bundle = makeDefaultData(ENDataManager.EventType.orderOut);
        bundle.putString(nTtileNameKey, "주문-이탈");
        bundle.putString(ENDataManager.Params.products, "");
        bundle.putString(ENDataManager.Params.memberId, "member_id");

        return bundle;
    }

    // ViewedProduct
    public static Bundle makeViewedProductData() {
        Bundle bundle = makeDefaultData(ENDataManager.EventType.viewedProduct);
        bundle.putString(nTtileNameKey, "본 상품");
        bundle.putAll(addProductList());

        return bundle;
    }

    // ModifyUserInfo
    public static Bundle makeModifyUserData() {
        Bundle bundle = makeDefaultData(ENDataManager.EventType.modifyUser);
        bundle.putString(nTtileNameKey, "회원정보 수정");
        bundle.putString(ENDataManager.Params.memberId, "member_id");
        bundle.putString(ENDataManager.Params.memberName, "member_name");
        bundle.putString(ENDataManager.Params.phoneNumber, "");
        bundle.putString(ENDataManager.Params.email, "");
        bundle.putBoolean(ENDataManager.Params.smsAllowed, false);
        bundle.putBoolean(ENDataManager.Params.emailAllowed, false);
        bundle.putString(ENDataManager.Params.birthday, "birthday");
        bundle.putString(ENDataManager.Params.gender, "남");

        return bundle;
    }

    // SignIn
    public static Bundle makeSignInData() {
        //TODO: 작업 필요
        Bundle bundle = makeDefaultData(ENDataManager.EventType.signIn);
        bundle.putString(nTtileNameKey, "로그인");
        bundle.putString(ENDataManager.Params.memberId, "member_id");

        return bundle;
    }


    // SignOut
    public static Bundle makeSignOutData() {
        Bundle bundle = makeDefaultData(ENDataManager.EventType.signOut);
        bundle.putString(nTtileNameKey, "회원탈퇴");
        bundle.putString(ENDataManager.Params.memberId, "member_id");

        return bundle;
    }


    // SignUp
    public static Bundle makeSignUpData() {
        Bundle bundle = makeDefaultData(ENDataManager.EventType.signUp);
        bundle.putString(nTtileNameKey, "회원가입");
        bundle.putString(ENDataManager.Params.memberId, "member_id");
        bundle.putString(ENDataManager.Params.memberName, "member_name");
        bundle.putString(ENDataManager.Params.phoneNumber, "");
        bundle.putString(ENDataManager.Params.email, "");
        bundle.putBoolean(ENDataManager.Params.smsAllowed, false);
        bundle.putBoolean(ENDataManager.Params.emailAllowed, false);
        bundle.putString(ENDataManager.Params.birthday, "birthday");
        bundle.putString(ENDataManager.Params.gender, "gender");

        return bundle;
    }


    // Visit
    public static Bundle makeVisitData() {
        Bundle bundle = makeDefaultData(ENDataManager.EventType.visit);
        bundle.putString(nTtileNameKey, "실행");
        bundle.putString(ENDataManager.Params.pvInName, "page_name_in");
        bundle.putString(ENDataManager.Params.deeplink, "deep_link");

        return bundle;
    }

    // PageView
    public static Bundle makePageViewData() {
        Bundle bundle = makeDefaultData(ENDataManager.EventType.pageView);
        bundle.putString(nTtileNameKey, "방문(PageView)");
        bundle.putString(ENDataManager.Params.pvInName, "page_name_in");
        bundle.putString(ENDataManager.Params.pvOutName, "page_name_out");

        return bundle;
    }

    // Out
    public static Bundle makeOutData() {
        Bundle bundle = makeDefaultData(ENDataManager.EventType.out);
        bundle.putString(nTtileNameKey, "종료");
        bundle.putString(ENDataManager.Params.pvOutName, "out_name");

        return bundle;
    }


    // Install
    public static Bundle makeInstallData() {
        Bundle bundle = makeDefaultData(ENDataManager.EventType.install);
        bundle.putString(nTtileNameKey, "설치");
        bundle.putString(ENDataManager.Params.referrer, "referrer");

        return bundle;
    }

}
