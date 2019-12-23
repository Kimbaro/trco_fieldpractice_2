# trco_fieldpractice_2
===================================

TRco 인턴쉽에서 개발한 Proto타입의 프로젝트

unitybuild_app :  마지막 수정일자 2019-12-01_02 로  Unity3D 안드로이드 변환 한 프로젝트와 안드로이드 Native Java Module를 import한 최종본

unity : Unity3D 프로젝트

server : Proto타입 어플리케이션에 활용된 server

module_app : 안드로이드 Native Java Module의 모듈을 담아둔 프로젝트

key : TRco GCP에 접근하기 위한 키 파일, 
===================================

# 호흡기 디바이스 커스텀 프로토콜
Receive type (블루투스 기기로 부터 수신된 값)
------------
* 각 수신 값 SU1,SU2,SU3 ... 등등 디바이스에서 수신된 값은 수정하지 말것 

- 초기설정 시 
SU1,SU2,SU3,SU4 
: 해당 설정 로직은 Lung_Coefficient.device_start(String str, byte[] arrayOfByte) 에 구성되어 있음.

- 호흡근력
SU5
: 해당 설정 로직은 Lung_Coefficient.device_muscle_check(String str, byte[] arrayOfByte, int test_kind) 에 진행되며
1. test_kind = 1 인경우 흡기압
2. test_kind = 2 인경우 호기압

- 폐기능
SU6,SU9
1. SU6은 폐활량 측정 시 실시간으로 기기로 부터 넘어오는 전기신호 데이터임. Lung_Coefficient.device_lung_check(String str, byte[] arrayOfByte) 다음의 함수에서 진행됨.

2. SU9는 측정 완료 시 기기로 부터 수신된 값이며 SU6에 의해 실시간으로 들어온 전기신호 데이터를 byte 변환하여 측정값 산출함.
Lung_Coefficient.device_lung_check(String str, byte[] arrayOfByte) 
 
Send type (모바일 기기에서 보낸 값)
------------
- 호흡근력(흡기압)
SA5E_1 : 흡기압 측정시 기기에 SA5E를 전달하며 이후 수신된 SU5 값의 처리함수에서 1인경우 흡기압, 2인경우 호기압 구분함. 구분 여부는 Lung_Coefficient.device_muscle_check() 에서 진행되며 변수 int test_kind에 구분 값 초기화됨.

- 호흡근력(호기압)
SA5E_2 : 호기압 측정시 기기에 SA5E를 전달함. 위에서 설명한 방식은 SA5E_1 과 같음.

- 폐기능
SA0E : 폐기능 측정시 기기에 SA0E를 전달하며 이후 실시간으로 기기로부터 프로펠러로 부터 발생한 전기신호(약 6초간) 실시간으로 수신함.

- 기기리셋?
SAFE : 디바이스 기기내 데이터 리셋 (추정 : 기기 제작자로 부터 자세한 정보 못받음.)

Webview (모바일내 Local환경.)
------------
* 모바일 내 'assets/www' 경로 참고

- 측정기록부분
HistoryActivity.class -> file:///android_asset/www/목록/index.html
목록구성에 있어서 AJAX통신을 위한 url IP주소(128 line) 수정해줘야함.

- 폐기능시각화부분
file:///android_asset/www/목록/index.html -> (모바일 로컬환경 폐기능검사)replace -> '../폐기능검사/index.html'
측정기록 리스트 아이템 클릭 시 로컬환경 내 폐기능검사/index.html 로 이동
 
- 호흡근력시각화부분
file:///android_asset/www/목록/index.html -> (모바일 로컬환경 호흡근측정)replace -> '../호흡근측정/index.html'
측정기록 리스트 아이템 클릭 시 로컬환경 내 호흡근측정/index.html 로 이동

Webview (외부 URL접근)
------------
* 서버 내 'resources/static/' 경로 참고
주소검색이나 지도 openAPI를 활용하기 위해선 로컬환경이 아닌 외부 환경으로 부터 접근해야함. WebServer로 접근.

- 주소검색부분
* 모바일에서 RestAPI 요청 webView.loadUrl(ServerConfig.IP + "/address_search", headers)
Server 내 FrontController.class에서 요청 처리하며 주소 검색을 위한 
다음의 경로를 Webview에 띄움 (Server단 경로)"classpath:/static/address_search/index.html"

- 주변병원찾기부분
* 모바일에서 Local페이지인 webView.loadUrl("file:///android_asset/www/지도/location_detail_check.html")
* 목록구성에 있어서 AJAX통신을 위한 url 수정해줘야함.
접근 후 'location_detail_check.html'에서  
var url = "http://IPADDRESS:8080/hospital_map"; 함수의 url로 replace.
Server 내 FrontController.class에서 요청 처리하며 주소 검색을 위한 
다음의 경로를 Webview에 띄움 (Server단 경로)"classpath:/static/hospital_map/index.html"

# 지도 openAPI KEY는 개인사용자용 이므로 개발 시 다시 발급받을 것.


