package com.datamanager.sample;

import android.icu.text.Edits;
import android.os.Bundle;
import android.os.Parcelable;

import com.enliple.datamanagersdk.ENDataManager;
import com.enliple.datamanagersdk.events.EventModel;
import com.enliple.datamanagersdk.events.models.ENCart;
import com.enliple.datamanagersdk.events.models.ENCustomEvent;
import com.enliple.datamanagersdk.events.models.ENFavorite;
import com.enliple.datamanagersdk.events.models.ENInstall;
import com.enliple.datamanagersdk.events.models.ENModifyUserInfo;
import com.enliple.datamanagersdk.events.models.ENOrder;
import com.enliple.datamanagersdk.events.models.ENOrderCancel;
import com.enliple.datamanagersdk.events.models.ENOrderOut;
import com.enliple.datamanagersdk.events.models.ENOut;
import com.enliple.datamanagersdk.events.models.ENPageView;
import com.enliple.datamanagersdk.events.models.ENProduct;
import com.enliple.datamanagersdk.events.models.ENSignIn;
import com.enliple.datamanagersdk.events.models.ENSignOut;
import com.enliple.datamanagersdk.events.models.ENSignUp;
import com.enliple.datamanagersdk.events.models.ENViewedProduct;
import com.enliple.datamanagersdk.events.models.ENVisit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class DataConvert {

    static DataConvert instance = null;



    public static DataConvert getInstance() {
        if (instance == null) {
            instance = new DataConvert();
        }
        return instance;
    }



    public EventModel convertData(Bundle bundle) {
        String type = bundle.getString(ENDataManager.Params.type);

        if (type == null) {
            return null;
        }

        switch (type) {
            case ENDataManager.EventType.pageView:          return convertPageViewPageViewEvent(bundle);
            case ENDataManager.EventType.out:               return convertOutEvent(bundle);
            case ENDataManager.EventType.install:           return convertInstallEvent(bundle);
            case ENDataManager.EventType.custom:            return convertCustomEvent(bundle);
            case ENDataManager.EventType.visit:             return convertVisitEvent(bundle);
            case ENDataManager.EventType.signUp:            return convertSignUpEvent(bundle);
            case ENDataManager.EventType.signIn:            return convertSignInEvent(bundle);
            case ENDataManager.EventType.signOut:           return convertSignOutEvent(bundle);
            case ENDataManager.EventType.modifyUser:        return convertModifyUserEvent(bundle);
            case ENDataManager.EventType.viewedProduct:     return convertViewedProductEvent(bundle);
            case ENDataManager.EventType.cart:              return convertCartEvent(bundle);
            case ENDataManager.EventType.favorite:          return convertFavoriteEvent(bundle);
            case ENDataManager.EventType.order:             return convertOrderEvent(bundle);
            case ENDataManager.EventType.orderOut:          return convertOrderOutEvent(bundle);
            case ENDataManager.EventType.orderCancel:       return convertOrderCancelEvent(bundle);

            default:
                return null;
        }
    }



    private ArrayList<ENProduct> convertProduct(Bundle bundle) {

        ArrayList<ENProduct> products = new ArrayList<>();
        ArrayList<Bundle> productBundle = bundle.getParcelableArrayList(ENDataManager.Params.products);

        if (productBundle == null || productBundle.isEmpty()) {
//            return new ENProduct[] {};
            return new ArrayList<ENProduct>();
        }

        for( Bundle data : productBundle) {
            ENProduct product = new ENProduct();

            Iterator<String> keys = data.keySet().iterator();
            while(keys.hasNext()) {
                String key = keys.next();
                if (key.equals(ENDataManager.Params.type) || key.equals(ENDataManager.Params.products)) {
                    continue;
                }
                else if(key.equals(ENDataManager.Params.productId)) {
                    product.setProductId(data.getString(ENDataManager.Params.productId));
                }
                else if(key.equals(ENDataManager.Params.productName)) {
                    product.setProductName(data.getString(ENDataManager.Params.productName));
                }
                else if(key.equals(ENDataManager.Params.productPrice)) {
                    product.setPrice(data.getInt(ENDataManager.Params.productPrice));
                }
                else if(key.equals(ENDataManager.Params.productImageUrl)) {
                    product.setImageUrl(data.getString(ENDataManager.Params.productImageUrl));
                }
                else if(key.equals(ENDataManager.Params.productUrl)) {
                    product.setProductUrl(data.getString(ENDataManager.Params.productUrl));
                }
                else if(key.equals(ENDataManager.Params.productQty)) {
                    product.setQuantity(data.getInt(ENDataManager.Params.productQty));
                }
                else {
                    product.addCustomData(key, data.getString(key));
                }
            }

            products.add(product);
        }

//        return products.toArray(new ENProduct[0]);
        return products;
    }




    private EventModel convertCustomEvent(Bundle bundle) {
        Iterator<String> keys = bundle.keySet().iterator();

        ENCustomEvent customEvent = new ENCustomEvent(bundle.getString(ENDataManager.Params.eventName));

        while(keys.hasNext()) {
            String key = keys.next();

            if (key.equals(ENDataManager.Params.type) || key.equals(PresetData.nTtileNameKey)) {
                continue;
            }
            else if (key.equals(ENDataManager.Params.products)) {
                customEvent.addCustomData(ENDataManager.Params.products, convertProduct(bundle));
            }
            else {
                customEvent.addCustomData(key, bundle.getString(key));
            }

        }

        return customEvent;
    }


    private EventModel convertPageViewPageViewEvent(Bundle bundle) {
        Iterator<String> keys = bundle.keySet().iterator();

        ENPageView pageView = new ENPageView(
                bundle.getString(ENDataManager.Params.pvInName),
                bundle.getString(ENDataManager.Params.pvOutName)
        );

        while(keys.hasNext()) {
            String key = keys.next();
            if (key.equals(ENDataManager.Params.type) || key.equals(PresetData.nTtileNameKey) ||
                    key.equals(ENDataManager.Params.pvInName) || key.equals(ENDataManager.Params.pvOutName)) {
                continue;
            }
            else if (key.equals(ENDataManager.Params.products)) {
                pageView.addCustomData(ENDataManager.Params.products, convertProduct(bundle));
            }
            else {
                pageView.addCustomData(key, bundle.getString(key));
            }

        }

        return pageView;
    }


    private EventModel convertOutEvent(Bundle bundle) {
        Iterator<String> keys = bundle.keySet().iterator();

        ENOut out = new ENOut(bundle.getString(ENDataManager.Params.pvOutName));
        while(keys.hasNext()) {
            String key = keys.next();

            if (key.equals(ENDataManager.Params.type) || key.equals(PresetData.nTtileNameKey) ||
                    key.equals(ENDataManager.Params.pvOutName)) {
                continue;
            }
            else {
                out.addCustomData(key, bundle.getString(key));
            }
        }

        return out;
    }


    private EventModel convertInstallEvent(Bundle bundle) {
        Iterator<String> keys = bundle.keySet().iterator();

        ENInstall install = new ENInstall(bundle.getString(ENDataManager.Params.referrer));
        while(keys.hasNext()) {
            String key = keys.next();

            if (key.equals(ENDataManager.Params.type) || key.equals(PresetData.nTtileNameKey) ||
                    key.equals(ENDataManager.Params.referrer)) {
                continue;
            }
            else {
                install.addCustomData(key, bundle.getString(key));
            }
        }

        return install;
    }


    private EventModel convertVisitEvent(Bundle bundle) {
        Iterator<String> keys = bundle.keySet().iterator();

        ENVisit visit = new ENVisit(bundle.getString(ENDataManager.Params.pvInName));

        while(keys.hasNext()) {
            String key = keys.next();

            if (key.equals(ENDataManager.Params.type) || key.equals(PresetData.nTtileNameKey)) {
                continue;
            }
            else if (key.equals(ENDataManager.Params.deeplink)) {
                visit.setDeepLink(bundle.getString(key));
            }
            else {
                visit.addCustomData(key, bundle.getString(key));
            }
        }

        return visit;
    }

    private EventModel convertSignUpEvent(Bundle bundle) {
        Iterator<String> keys = bundle.keySet().iterator();

        ENSignUp signUp = new ENSignUp();

        while(keys.hasNext()) {
            String key = keys.next();

            if (key.equals(ENDataManager.Params.type) || key.equals(PresetData.nTtileNameKey)) {
                continue;
            }
            else if (key.equals(ENDataManager.Params.birthday)) {
                signUp.setBirthday(bundle.getString(ENDataManager.Params.birthday));
            }
            else if (key.equals(ENDataManager.Params.email)) {
                signUp.setEmail(bundle.getString(ENDataManager.Params.email));
            }
            else if (key.equals(ENDataManager.Params.emailAllowed)) {
                signUp.setEmailAllowed(bundle.getBoolean(ENDataManager.Params.emailAllowed));
            }
            else if (key.equals(ENDataManager.Params.gender)) {
                signUp.setGender(bundle.getString(ENDataManager.Params.gender));
            }
            else if (key.equals(ENDataManager.Params.memberId)) {
                signUp.setMemberId(bundle.getString(ENDataManager.Params.memberId));
            }
            else if (key.equals(ENDataManager.Params.memberName)) {
                signUp.setMemberName(bundle.getString(ENDataManager.Params.memberName));
            }
            else if (key.equals(ENDataManager.Params.phoneNumber)) {
                signUp.setPhoneNumber(bundle.getString(ENDataManager.Params.phoneNumber));
            }
            else if (key.equals(ENDataManager.Params.smsAllowed)) {
                signUp.setSmsAllowed(bundle.getBoolean(ENDataManager.Params.smsAllowed));
            }
            else {
                signUp.addCustomData(key, bundle.getString(key));
            }
        }

        return signUp;
    }


    private EventModel convertSignInEvent(Bundle bundle) {
        Iterator<String> keys = bundle.keySet().iterator();

        ENSignIn signIn = new ENSignIn();

        while(keys.hasNext()) {
            String key = keys.next();

            if (key.equals(ENDataManager.Params.type) || key.equals(PresetData.nTtileNameKey)) {
                continue;
            }
            else if (key.equals(ENDataManager.Params.memberId)) {
                signIn.setMemberId(bundle.getString(ENDataManager.Params.memberId));
            }
            else {
                signIn.addCustomData(key, bundle.getString(key));
            }
        }
        return signIn;
    }


    private EventModel convertSignOutEvent(Bundle bundle) {
        Iterator<String> keys = bundle.keySet().iterator();

        ENSignOut signOut = new ENSignOut();

        while(keys.hasNext()) {
            String key = keys.next();

            if (key.equals(ENDataManager.Params.type) || key.equals(PresetData.nTtileNameKey)) {
                continue;
            }
            else if (key.equals(ENDataManager.Params.memberId)) {
                signOut.setMemberId(bundle.getString(ENDataManager.Params.memberId));
            }
            else {
                // convertCustomEvent
                signOut.addCustomData(key, bundle.getString(key));
            }
        }

        return signOut;
    }


    private EventModel convertModifyUserEvent(Bundle bundle) {
        Iterator<String> keys = bundle.keySet().iterator();

        ENModifyUserInfo modifyUserInfo = new ENModifyUserInfo();

        while(keys.hasNext()) {
            String key = keys.next();

            if (key.equals(ENDataManager.Params.type) || key.equals(PresetData.nTtileNameKey)) {
                continue;
            }
            else if (key.equals(ENDataManager.Params.birthday)) {
                modifyUserInfo.setBirthday(bundle.getString(ENDataManager.Params.birthday));
            }
            else if (key.equals(ENDataManager.Params.email)) {
                modifyUserInfo.setEmail(bundle.getString(ENDataManager.Params.email));
            }
            else if (key.equals(ENDataManager.Params.emailAllowed)) {
                modifyUserInfo.setEmailAllowed(bundle.getBoolean(ENDataManager.Params.emailAllowed));
            }
            else if (key.equals(ENDataManager.Params.gender)) {
                modifyUserInfo.setGender(bundle.getString(ENDataManager.Params.gender));
            }
            else if (key.equals(ENDataManager.Params.memberId)) {
                modifyUserInfo.setMemberId(bundle.getString(ENDataManager.Params.memberId));
            }
            else if (key.equals(ENDataManager.Params.memberName)) {
                modifyUserInfo.setMemberName(bundle.getString(ENDataManager.Params.memberName));
            }
            else if (key.equals(ENDataManager.Params.phoneNumber)) {
                modifyUserInfo.setPhoneNumber(bundle.getString(ENDataManager.Params.phoneNumber));
            }
            else if (key.equals(ENDataManager.Params.smsAllowed)) {
                modifyUserInfo.setSmsAllowed(bundle.getBoolean(ENDataManager.Params.smsAllowed));
            }
            else {
                modifyUserInfo.addCustomData(key, bundle.getString(key));
            }
        }

        return modifyUserInfo;
    }


    private EventModel convertViewedProductEvent(Bundle bundle) {
        Iterator<String> keys = bundle.keySet().iterator();

        ENViewedProduct viewedProduct = new ENViewedProduct();

        while(keys.hasNext()) {
            String key = keys.next();

            if (key.equals(ENDataManager.Params.type) || key.equals(PresetData.nTtileNameKey)) {
                continue;
            }
            else if (key.equals(ENDataManager.Params.productImageUrl)) {
                viewedProduct.setImageUrl(bundle.getString(ENDataManager.Params.productImageUrl));
            }
            else if (key.equals(ENDataManager.Params.productPrice)) {
                viewedProduct.setPrice(bundle.getInt(ENDataManager.Params.productPrice));
            }
            else if (key.equals(ENDataManager.Params.productId)) {
                viewedProduct.setProductId(bundle.getString(ENDataManager.Params.productId));
            }
            else if (key.equals(ENDataManager.Params.productName)) {
                viewedProduct.setProductName(bundle.getString(ENDataManager.Params.productName));
            }
            else if (key.equals(ENDataManager.Params.productUrl)) {
                viewedProduct.setProductUrl(bundle.getString(ENDataManager.Params.productUrl));
            }
            else if (key.equals(ENDataManager.Params.productQty)) {
                viewedProduct.setQuantity(bundle.getInt(ENDataManager.Params.productQty));
            }
            else {
                viewedProduct.addCustomData(key, bundle.getString(key));
            }
        }

        return viewedProduct;
    }


    private EventModel convertCartEvent(Bundle bundle) {
        Iterator<String> keys = bundle.keySet().iterator();

        ENCart cart = new ENCart();

        while(keys.hasNext()) {
            String key = keys.next();

            if (key.equals(ENDataManager.Params.type) || key.equals(PresetData.nTtileNameKey)) {
                continue;
            }
            else if (key.equals(ENDataManager.Params.productImageUrl)) {
                cart.setImageUrl(bundle.getString(ENDataManager.Params.productImageUrl));
            }
            else if (key.equals(ENDataManager.Params.productPrice)) {
                cart.setPrice(bundle.getInt(ENDataManager.Params.productPrice));
            }
            else if (key.equals(ENDataManager.Params.productId)) {
                cart.setProductId(bundle.getString(ENDataManager.Params.productId));
            }
            else if (key.equals(ENDataManager.Params.productName)) {
                cart.setProductName(bundle.getString(ENDataManager.Params.productName));
            }
            else if (key.equals(ENDataManager.Params.productUrl)) {
                cart.setProductUrl(bundle.getString(ENDataManager.Params.productUrl));
            }
            else if (key.equals(ENDataManager.Params.productQty)) {
                cart.setQuantity(bundle.getInt(ENDataManager.Params.productQty));
            }
            else {
                cart.addCustomData(key, bundle.getString(key));
            }
        }

        return cart;
    }


    private EventModel convertFavoriteEvent(Bundle bundle) {
        Iterator<String> keys = bundle.keySet().iterator();

        ENFavorite favorite = new ENFavorite();

        while(keys.hasNext()) {
            String key = keys.next();

            if (key.equals(ENDataManager.Params.type) || key.equals(PresetData.nTtileNameKey)) {
                continue;
            }
            else if (key.equals(ENDataManager.Params.productImageUrl)) {
                favorite.setImageUrl(bundle.getString(ENDataManager.Params.productImageUrl));
            }
            else if (key.equals(ENDataManager.Params.productPrice)) {
                favorite.setPrice(bundle.getInt(ENDataManager.Params.productPrice));
            }
            else if (key.equals(ENDataManager.Params.productId)) {
                favorite.setProductId(bundle.getString(ENDataManager.Params.productId));
            }
            else if (key.equals(ENDataManager.Params.productName)) {
                favorite.setProductName(bundle.getString(ENDataManager.Params.productName));
            }
            else if (key.equals(ENDataManager.Params.productUrl)) {
                favorite.setProductUrl(bundle.getString(ENDataManager.Params.productUrl));
            }
            else if (key.equals(ENDataManager.Params.productQty)) {
                favorite.setQuantity(bundle.getInt(ENDataManager.Params.productQty));
            }
            else {
                favorite.addCustomData(key, bundle.getString(key));
            }
        }

        return favorite;
    }


    private EventModel convertOrderEvent(Bundle bundle) {
        Iterator<String> keys = bundle.keySet().iterator();

        ENOrder order = new ENOrder();
        while(keys.hasNext()) {
            String key = keys.next();

            if (key.equals(ENDataManager.Params.type) || key.equals(PresetData.nTtileNameKey)) {
                continue;
            }
            else if (key.equals(ENDataManager.Params.products)) {
                order.addProducts(convertProduct(bundle));
            }
            else if (key.equals(ENDataManager.Params.address)) {
                order.setAddress(bundle.getString(ENDataManager.Params.address));
            }
            else if (key.equals(ENDataManager.Params.email)) {
                order.setEmail(bundle.getString(ENDataManager.Params.email));
            }
            else if (key.equals(ENDataManager.Params.memberId)) {
                order.setMemberId(bundle.getString(ENDataManager.Params.memberId));
            }
            else if (key.equals(ENDataManager.Params.memberName)) {
                order.setMemberName(bundle.getString(ENDataManager.Params.memberName));
            }
            else if (key.equals(ENDataManager.Params.orderId)) {
                order.setOrderId(bundle.getString(ENDataManager.Params.orderId));
            }
            else if (key.equals(ENDataManager.Params.paymentMethod)) {
                order.setPaymentMethod(bundle.getString(ENDataManager.Params.paymentMethod));
            }
            else if (key.equals(ENDataManager.Params.phoneNumber)) {
                order.setPhoneNumber(bundle.getString(ENDataManager.Params.phoneNumber));
            }
            else if (key.equals(ENDataManager.Params.totalPrice)) {
                order.setTotalPrice(bundle.getInt(ENDataManager.Params.totalPrice));
            }
            else if (key.equals(ENDataManager.Params.totalQuantity)) {
                order.setTotalQuantity(bundle.getInt(ENDataManager.Params.totalQuantity));
            }
            else if (key.equals(ENDataManager.Params.zipCode)) {
                order.setZipCode(bundle.getString(ENDataManager.Params.zipCode));
            }
            else {
                order.addCustomData(key, bundle.getString(key));
            }
        }

        return order;
    }


    private EventModel convertOrderCancelEvent(Bundle bundle) {
        Iterator<String> keys = bundle.keySet().iterator();

        ENOrderCancel orderCancel = new ENOrderCancel();

        while(keys.hasNext()) {
            String key = keys.next();

            if (key.equals(ENDataManager.Params.type) || key.equals(PresetData.nTtileNameKey)) {
                continue;
            }
            else if (key.equals(ENDataManager.Params.products)) {
                orderCancel.addProducts(convertProduct(bundle));
            }
            else if (key.equals(ENDataManager.Params.memberId)) {
                orderCancel.setMemberId(bundle.getString(ENDataManager.Params.memberId));
            }
            else if (key.equals(ENDataManager.Params.orderId)) {
                orderCancel.setOrderId(bundle.getString(ENDataManager.Params.orderId));
            }
            else {
                orderCancel.addCustomData(key, bundle.getString(key));
            }
        }

        return orderCancel;
    }


    private EventModel convertOrderOutEvent(Bundle bundle) {
        Iterator<String> keys = bundle.keySet().iterator();

        ENOrderOut orderOut = new ENOrderOut();

        while(keys.hasNext()) {
            String key = keys.next();

            if (key.equals(ENDataManager.Params.type) || key.equals(PresetData.nTtileNameKey)) {
                continue;
            }
            else if (key.equals(ENDataManager.Params.products)) {
                orderOut.addProducts(convertProduct(bundle));
            }
            else if (key.equals(ENDataManager.Params.memberId)) {
                orderOut.setMemberId(bundle.getString(ENDataManager.Params.memberId));
            }
            else {
                // convertCustomEvent
                orderOut.addCustomData(key, bundle.getString(key));
            }
        }

        return orderOut;
    }

}
