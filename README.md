# DataManager Android SDK
DataManagerSDK 를 이용하여 광고를 노출하는 방법을 제공하고 있습니다.  


## 최신 버전 및 변경사항
- 최신버전 : 1.0.4
- 변경사항 : Bug Fix
<br>

## 개발환경
- 최소 SDK Version : Android 26
- Compile SDK : Android 34
- Build Tool : Android Studio 
- androidX 권장


## 1. SDK 기본설정

- project build.gradle 에 mavenCentral() 을 추가합니다.

```XML
allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
```

- app build.gradle 에 DataManager SDK를 추가합니다. play-services-ads-identifier와 installreferrer도 설정해 주셔야 합니다.
```XML
dependencies {
  ...
  implementation 'io.github.tune720:DataManagerSDK:1.0.4' 

  implementation 'com.google.android.gms:play-services-ads-identifier:17.0.0'
  implementation 'com.android.installreferrer:installreferrer:2.2'

  ...
}
```
만약, 빌드시 gradle dependencies에 의한 오류 발생시 아래를 참고하시기 바랍니다.
```XML
  implementation 'io.github.tune720:DataManagerSDK:1.0.4' {
    transitive = false
  }
```


## 2. SDK 초기화 및 사용방법

### 1) SDK 초기화

아래 함수를 통해 SDK를 초기화 시켜 줍니다.  
서비스 키의 경우 [DataManager 관리자 페이지(tune720.com)](https://tune720.com)에서 확인 가능 합니다.
```Java
ENDataManager.init(this, "{ 서비스키 }");
```
* SDK를 사용하기 전에 반드시 초기화를 진행해 주셔야 합니다.  
  SDK 초기화 없이 각종 기능 이용시 IllegalArgumentException 이 발생할 수 있습니다.


### 2) 이벤트 생성 및 전달
* 기본 이벤트  
  기본 이벤트는 본 SDK를 사용하는데 있어 대표적인 기능들에 대해 미리 생성해둔 이벤트에 해당 합니다.  
  아래와 같이 미리 생성된 클래스를 활용하셔서 이벤트를 생성 전달 하실 수 있습니다.  
  ```Java

  ENCart cart = new ENCart();

  //기본 파라메터 설정
  cart.setImageUrl("image url");
  cart.setPrice(10000);
  cart.setProductId("productId");
  cart.setProductName("product name");
  cart.setProductUrl("product url");
  cart.setQuantity(1);

  //커스텀 파라메터 추가
  cart.addCustomData(key, bundle.getString(key));

  //생성된 이벤트 전달
  ENDataManager.getInstance().addEvent(cart);


  /**
   * ENProduct 리스트를 전달하는 경우
   */

  //ENProduct 생성 및 각 항목값 설정
  ENProduct product = new ENProduct(); 
  product.setProductId("product_id");
  product.setProductName("상품명")
  ...

  //ArrayList에 추가
  ArrayList<ENProduct> productArray = new ArrayList<>();
  productArray.add(product)

  //ENOrder 객체 생성
  ENOrder order = new ENOrder();
  order.addProducts(productArray)  // porduct 리스트 설정

  // addProducts()가 없는 객체에서 ENProduct를 설정잎 필요한 경우 아래 방법을 사용할 수 있습니다.
  // 관리자 페이지 설정에서 products를 받도록 설정하지 않으면 적용이 되지 않는점 참고 바랍니다.
  order.addCustomData(ENDataManager.Params.products, ArrayList<ENProduct>) 
  ...

  //생성된 이벤트 전달
  ENDataManager.getInstance().addEvent(order);

  ```
  <br>

* 커스텀 이벤트  
  커스텀 이벤트는 사용자가 별도로 생성한 이벤트를 말합니다.    
  해당 이벤트의 경우 아래와 같이 ENCustomEvent를 이용하여 이벤트를 생성 후 전달 하시면 됩니다.  
  커스텀 이벤트의 경우 다수 생성될 수 있으므로, 해당 이벤트의 구분을 위해 EventName을 추가로 전달 받습니다.  
  EventName의 경우 커스텀 이벤트를 생성하실때 사용하신 이벤트명을 넣어 주시면 됩니다.
  ```Java
  ENCustomEvent customEvent = new ENCustomEvent("EventName");
  
  //커스텀 파라메터 추가
  customEvent.addCustomData(key, bundle.getString(key));

  //생성된 이벤트 전달
  ENDataManager.getInstance().addEvent(customEvent);
  ```
  <br>

* 이벤트 전달  
  모든 이벤트 관련 클래스는 EventModel을 상속받고 있으므로 아래와 같이 생성된 이벤트들을 전달하시면 됩니다.
  ```Java
  EventModel cart = new ENCart();
  ENDataManager.getInstance().addEvent(cart);
  ```
  <br>

* 이벤트와 파라메터는 아래 <b>3)이벤트 타입</b> 과 <b>4)파라메터 종류</b>를 참고하시면 되며,  
  기본 이벤트의 경우 set 함수를 통해 기본 설정 값들에 대해 지원 하고 있으니 참고 바랍니다.  
  파라메터의 경우 커스텀하여 추가된 Key값을 가지는 경우 제공된 값을 사용하지 않고  
  아래 <b>'커스텀 파라메터 추가'</b>와 같이 직접 입력하시면 됩니다. 
  <br>

* 커스텀 파라메터의 추가  
  사용자 지정된 커스텀 파라메터의 경우 아래 함수를 통해 추가하시면 됩니다.
  ```Java
  eventModel.addCustomData("customKey", "customValue");
  ```
  예시의 경우 Value에서 String데이터를 세팅하도록 되어 있으나 다양한 타입을 지원하고 있습니다.
  <br>
  <br>
  <br>

### 3) 이벤트 타입
| 클래스             | 구분            | 필수 <br> 파라메터        |        설명         |
| :-------          | :---------:   | :-------------:    | :------------------------ |
| ENSignIn          |   기본         | memberId | 사용자 로그인 |
| ENSignOut         |   기본         | memberId | 회원 탈퇴 |
| ENSignUp          |   기본         | memberId | 회원 가입 |
| ENModifyUserInfo  |   기본         | memberId | 사용자 정보 수정  |
| ENProduct         |   기본         | productId <br> productName <br> productPrice <br> productQty | 상품관련 이벤트에서 상품정보를 담아 주기 위해 사용. <br> ENOrder, ENOrderOut, ENOrderCancel에서 사용되며, 별도 이벤트로 처리되지는 않습니다. |
| ENViewedProduct   |   기본         | productId <br> productName <br> productPrice <br> productImageUrl <br> productUrl | 상품에 대한 상세 보기등을 처리하기 위한 이벤트 |
| ENFavorite        |   기본         | productId <br> productName <br> productPrice <br> productImageUrl <br> productUrl | 상품에 대한 좋아요(즐겨찾기) 관련 이벤트  |
| ENCart            |   기본         | productId <br> productName <br> productPrice <br> productQty <br> productImageUrl <br> productUrl| 장바구니 이벤트  |
| ENOrder           |   기본         | orderId <br> totalPrice <br> totalQuantity <br> products | 상품 구매 이벤트(구매 화면으로 이동) |
| ENOrderCancel     |   기본         | orderId <br> products | (결제한)구매 취소  |
| ENOrderOut        |   기본         | products | 구매 화면에서 결제를 하지 않고 빠져나간 경우 |
| ENCustomEvent     |   사용자 지정    | 없음 |  사용자 지정 이벤트  |
| ENInstall         |   SDK 자체 처리 | 없음  | 앱 설치 이벤트  |
| ENVisit           |   SDK 자체 처리 | 없음 | 앱 실행 (외부에서 앱 진입 등) |
| ENPageView        |   SDK 자체 처리 | 없음  | 화면 전환 이벤트 ( Activity 단위 처리) |
| ENOut             |   SDK 자체 처리 | 없음 | 앱 종료 이벤트 |
| ENDeepLinkOpen    |   SDK 자체 처리 | 없음 | DeepLink를 통한 앱 실행 이벤트 |
 
* SDK 자체 처리 타입의 경우 특별한 경우가 아니라면 직접 이벤트를 만들어서 전달하실 필요가 없습니다.
* 필수 파라메터의 경우 반드시 지정되어야 하는 값 입니다.  해당값을 할당하기 위한 함수들이 있으며 해당 함수를 통해 값을 지정해주시면 됩니다.
* 필수 파라메터가 없는 경우 각 클래스 생성시 받는 값외에 반드시 지정해주어야 할 값들이 없는 경우 입니다.
* 파라메터에서 products의 경우 이벤트 클래스의 addProducts(ArrayList<ENProduct> products)를 이용하시거나,  
  addCustomData(ENDataManager.Params.products, ArrayList<ENProduct>) 를 사용하시면 됩니다.
<br><br>


### 4) 파라메터
* Trigger : 이벤트 발생 조건값 입니다.
  | 구분         |        설명         |
  | :-------    | :------------------------ |
  | click       | 버튼 클릭등 사용자 터치 이벤트로 발생  |
  | loadPage    | 화면 전환과 같이 보여지는 화면의 변화 관련 이벤트  |
<br>

* Parameter
  | 구분             |        설명         |
  | :-------        | :------------------------ |
  | birthday        | 생일  |
  | email           | E-Mail 주소  |
  | emailAllowed    | E-Mail 수신 동의 여부  |
  | eventName       | 이벤트 명. 커스텀 이벤트들의 구분을 위해 활용  |
  | gender          | 성별  |
  | memberId        | 사용자 아이디  |
  | memberName      | 사용자 이름  |
  | orderId         | 주문 번호  |
  | paymentAmount   | 총 결제 금액  |
  | paymentMethod   | 결제 수단  |
  | phoneNumber     | 핸드폰(전화) 번호  |
  | productId       | 상품 코드  |
  | productImageUrl | 상품 이미지 URL  |
  | productName     | 상품명  |
  | productPrice    | 상품 가격  |
  | productQty      | 상품 수량  |
  | products        | 상품 목록에 대한 키값 (상품과 관련된 이벤트에서 상품 목록 관리)  |
  | productUrl      | 상품 URL  |
  | smsAllowed      | SMS 수신 동의 여부  |
  | zipCode         | 우편 번호  |
  | address         | 집 주소  |
  | totalPrice      | 상품 총 가격  |
  | totalQuantity   | 상품 총 수량  |
<br>


### 5) WebView를 사용하는 하이브리드 앱에서의 설정
웹 기반의 하이브리드앱의 경우 이미 삽입된 Script에 의해 데이터를 수집하게 됩니다.  
다만, 웹브라우져가 아닌 앱으로 접속한것임을 구분하기 위한 일부 설정이 필요하게 되며, 이를 위해 아래와 같이 설정해 주셔야 합니다.

```Java

String url = "웹뷰에 로딩할 Url"

WebView webView = findViewById(R.id.webView);
WebSettings settings = mWebView.getSettings();
settings.setJavaScriptEnabled(true);
settings.setDomStorageEnabled(true);


mWebView.setWebViewClient(new WebViewClient() {
  ... 

  // 페이지 로딩 시작 (필수)
  @Override
  public void onPageStarted(WebView view, String url, Bitmap favicon) {
    // 페이지 로딩이 시작될때 아래와 같이 페이지 로딩이 시작되었음을 알려줍니다.
    ENDataManager.getInstance().onWebViewPageStarted(view, url);
    super.onPageStarted(view, url, favicon);
  }

  //페이지 로딩 완료 알림 (선택사항)
  @Override
  public void onPageFinished(WebView view, String url) {
    // 페이지 로딩이 끝난뒤 아래와 같이 페이지 로딩이 끝났음을 알려 줍니다.
    ENDataManager.getInstance().onWebViewPageFinished(view, url);
    super.onPageFinished(view, url);
  }

  ...
});


// 페이지 로딩전 아래와 같이 호출해 주셔야 합니다.
ENDataManager.getInstance().setWebView(webView, url);
webView.loadUrl(url);

```

<br>
<br>

### 6) ENPageView 이벤트
ENPageView 이벤트는 화면전환에 대한 이벤트로, SDK에서 직접 처리하고 있으나 Activity를 기반으로 동작합니다.  
따라서 앱내 페이지 전환등이 하나의 Activity안에서 Fragment를 기반으로 이루어지거나 이와 유사하게 동작하는 경우,  
화면 전환에 대한 이벤트가 제대로 확인되지 않을 수 있습니다.
만약 화면 전환등이 Activity를 기반으로 발생하지 않는 경우 아래와 같이 별도로 이벤트를 추가하여 화면 전환에 대한 이벤트를 추적해야 합니다.
```Java

ENPageView pageView = new ENPageView("현재 노출된 화면 이름", "이전에 노출된 화면 이름");
ENDataManager.getInstance().addEvent(pageView);

```
* Activity에서 처음열린 Fragment등, 이전에 노출된 화면 이름을 특별히 지정하기 어려운 경우 빈값 또는 Activity 이름을 지정하시면 됩니다.


<br>
<br>

### 7) Sample 앱에 대하여  
본 SDK를 사용하는 방법에 대한 예시 앱으로 참고 하시면 됩니다.


<br>
<br>
<br>

# DataManager Android SDK Release History
 | version |        Description        |
 | :-----: | :------------------------ |
 | 1.0.4   |  Bug Fix        |
 | 1.0.3   |  Bug Fix        |
 | 1.0.2   |  Bug Fix        |
 | 1.0.1   |  Bug Fix        |
 | 1.0.0   |  최초 배포        |
