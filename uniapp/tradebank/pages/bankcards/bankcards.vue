<template>
<<<<<<< HEAD
	<view   >
		<view>
			<u-subsection class="subsection" :list="list" mode="button" :current="current" @change="sectionChange"></u-subsection>
		</view>
		
		<view  v-if="current==0" class="box">
<!-- 龙卡通==================================================== -->
			<view class="box1"  >
			    <view v-if="!cards1">
					<u--text style="padding-top: 5%;" align="center" type="info" text="您尚未开通龙卡通"></u--text>
					<view class="u-demo-block__content" style="padding-top: 5%;padding-bottom: 1%;">
					    <u-row>
					        <u-col span="6">
					            <u-button plain style="width: 80%;" type="" text="查看详情" @click=" tip(1)"></u-button>
					        </u-col>
					        <u-col span="6">
					            <u-button plain style="width: 80%;" type="" text="开通" @click="openCreateCard(1)"></u-button>
					        </u-col>
					    </u-row>
					</view>
				</view>
			    
				<view v-if="cards1">
					<u--text style="padding-top: 5%;padding-left: 20%;" align="left" type="info" :text="'龙卡通（'+cardsText[0]+'）'"></u--text>
					<view class="u-demo-block__content" style="padding-top: 5%;padding-bottom: 1%;">
					    <u-row>
					        <u-col span="3">
					            <u-button plain style="width: 80%;" type="" text="明细" @click="detail(1)" ></u-button>
					        </u-col>
					        <u-col span="3">
					            <u-button plain style="width: 80%;" type="" text="转账" @click="toTransfer(1)"  ></u-button>
					        </u-col>
							<u-col span="3">
							    <u-button plain style="width: 80%;" type="" text="存取" @click="opneoutandsave(1)" ></u-button>
							</u-col>
							<u-col span="3">
							    <u-button plain style="width: 80%;" type="" text="理财" ></u-button>
							</u-col>
					    </u-row>
					</view>
				</view>
			</view>
	<!-- 养老金==================================================== -->		
			<view class="box2" >		
			    <view v-if="!cards2">
					<u--text style="padding-top: 5%;" align="center" type="info" text="您尚未开通个人养老金"></u--text>
					<u-button plain style="width: 80%;" type="" text="查看详情" @click=" tip(2)"></u-button>
				</view>
			</view>
			
			
			
<!-- 各种弹出层============================================================ -->
			<u-popup  class="popup" :show="show1" :round="10" mode="center"    @close="close" @open="open">
			        <view   style="padding: 10%;width: 80%;">
			            <u--text style="margin-top: -8%;" align="center" type="info" size="12" :text="mig"></u--text>
			        </view>
			</u-popup>
			<u-toast ref="uToast2"></u-toast>
	<!-- 存取的弹出层 -->
	        <u-modal :closeOnClickOverlay="true" :showConfirmButton="false"  @close="showoutandsave=false" :show="showoutandsave" title="存取页面" :content='content'>
			    <view style="display: block;width: 120%;">
					<u--text size="16" align="center" type="info" :text="'剩余金额(单位元)： '+balance"></u--text>
					<view style="padding-top: 5%;">
						<u--input
						    type="number"
						    placeholder="请输入金额"
						    border="surround"
						    v-model="saveorout"
						    @change="change"
						></u--input>
					</view>
					<view style="display: flex;padding-top: 2%;">
						<u-button style="flex: 2;"  type="warning" plain text="取钱" @click="outorsave(2)" ></u-button>
						<text style="flex: 1;"></text>
						<u-button  style="flex: 2;" type="success" plain text="存钱" @click="outorsave(1)" ></u-button>
					</view>
				</view>
				<u-toast ref="uToastout"></u-toast>
			</u-modal>
	<!-- 开户的弹出层 -->		
		    <u-popup class="popup2" :show="show2" :round="5" mode="bottom"   @close="close2" @open="open2">
					<u-toast ref="uToast"></u-toast>
					<view  style="padding: 5%;width: 90%;">
		                <view>
							    <u--text bold text="信息验证"></u--text>
		                		<!-- 注意，如果需要兼容微信小程序，最好通过setRules方法设置rules规则 -->
		                		<u--form
		                				labelPosition="left"
		                				:model="model"
		                				ref="uForm"
		                		>
		                			<u-form-item
		                					label="姓名"
		                					prop="userInfo.name"
		                					borderBottom
											
		                					ref="item1"
		                			>
		                				<u--input
										border="bottom"
		                						v-model="model.userInfo.name"
		                				></u--input>
		                			</u-form-item>
									<u-form-item
											label="身份证号码"
											prop="userInfo.idnumber"
											borderBottom
											ref="item1"
									>
										<u--input 
												v-model="model.userInfo.idnumber"
										></u--input>
									</u-form-item>
									<u-form-item
											label="电话号码"
											prop="mobile"
											borderBottom
											ref="item1"
									>
										<u--input
										        type="number"
												v-model="model.userInfo.phone"
										>
										<template slot="suffix">
															<u-code
																ref="uCode"
																@change="codeChange"
																seconds="60"
																changeText="X秒重新获取"
															></u-code>
															<u-button
																@tap="getCode"
																:text="tips"
																type="success"
																size="mini"
															></u-button>
														</template>
										</u--input>
									</u-form-item>
									<u-form-item label="验证码" ref="item1" v-if="flag==true">
										<u-code-input @finish="finish" style="margin: 0 auto;" mode="line" v-model="codewords" :maxlength="6"></u-code-input>
									</u-form-item>
		                		</u--form>
		                	</view>
		            </view>
		    </u-popup>	
			<u-modal @confirm="createCard(type)" @cancel="cancel" :show="show3" :title="title" content="是否确认开通？开通后注销账户需到线下柜台处持身份证及相应证件办理" showCancelButton="true" ></u-modal>
		
		</view>
		
<!-- 分割信用卡======================================================================================================== -->
		
		<view v-if="current==1">
			信用卡
		</view>
		
	</view>
=======
	<view   >
		<u-sticky bgColor="#fff">
		   <u-subsection class="subsection" :list="list" mode="button" :current="current" @change="sectionChange"></u-subsection>
		</u-sticky>
		<view v-if="current==0" class="background1" :style="{height:screenHeight}" >
			<view   class="box">
			<!-- 龙卡通==================================================== -->
						<view class="box1"  >
						    <view v-if="!cards1">
								<u--text style="padding-top: 5%;" align="center" type="info" text="您尚未开通龙卡通"></u--text>
								<view class="u-demo-block__content" style="padding-top: 5%;padding-bottom: 1%;">
								    <u-row>
								        <u-col span="6">
								            <u-button plain style="width: 80%;" type="" text="查看详情" @click=" tip(1)"></u-button>
								        </u-col>
								        <u-col span="6">
								            <u-button plain style="width: 80%;" type="" text="开通" @click="openCreateCard(1)"></u-button>
								        </u-col>
								    </u-row>
								</view>
							</view>
						    
							<view v-if="cards1">
								<u--text style="padding-top: 5%;padding-left: 20%;" align="left" type="info" :text="'龙卡通（'+cardsText[0]+'）'"></u--text>
								<view class="u-demo-block__content" style="padding-top: 5%;padding-bottom: 1%;">
								    <u-row>
								        <u-col span="3">
								            <u-button plain style="width: 80%;" type="" text="明细" @click="detail(1)" ></u-button>
								        </u-col>
								        <u-col span="3">
								            <u-button plain style="width: 80%;" type="" text="转账" @click="toTransfer(1)"  ></u-button>
								        </u-col>
										<u-col span="3">
										    <u-button plain style="width: 80%;" type="" text="存取" @click="opneoutandsave(1)" ></u-button>
										</u-col>
										<u-col span="3">
										    <u-button plain style="width: 80%;" type="" text="贷款" @click="toloans(1)" ></u-button>
										</u-col>
								    </u-row>
								</view>
							</view>
						</view>
				<!-- 养老金==================================================== -->		
						<view class="box2" >		
						    <view v-if="!cards2">
								<u--text style="padding-top: 5%;" align="center" type="info" text="您尚未开通个人养老金"></u--text>
								<u-button plain style="width: 80%;" type="" text="查看详情" @click=" tip(2)"></u-button>
							</view>
						</view>
					</view>
		</view>
		
<!-- 分割信用卡======================================================================================================== -->
		
		<view v-if="current==1" class="background2" :style="{height:screenHeight}" >
			<view  class="box4">
				
				<view class="box3"  >
				    <view v-if="!cards3">
						<u--text style="padding-top: 5%;" align="center" type="info" text="您尚未开通信用卡"></u--text>
						<view class="u-demo-block__content" style="padding-top: 5%;padding-bottom: 1%;">
						    <u-row>
						        <u-col span="6">
						            <u-button plain style="width: 80%;" type="" text="查看详情" @click=" tip(3)"></u-button>
						        </u-col>
						        <u-col span="6">
						            <u-button plain style="width: 80%;" type="" text="开通" @click="openCreateCard(3)"></u-button>
						        </u-col>
						    </u-row>
						</view>
					</view>
				    
					<view v-if="cards3">
						<u--text style="padding-top: 5%;padding-left: 20%;" align="left" type="info" :text="'信用卡（'+cardsText[2]+'）'"></u--text>
						<view class="u-demo-block__content" style="padding-top: 5%;padding-bottom: 1%;">
						    <u-row>
						        <u-col span="3">
						            <u-button plain style="width: 80%;" type="" text="明细" @click="detail(3)" ></u-button>
						        </u-col>
						        <u-col span="3">
						            <u-button plain style="width: 80%;" type="" text="还款" @click="returnm(3)"  ></u-button>
						        </u-col>
								<u-col span="3">
								    <u-button plain style="width: 80%;" type="" text="存取" @click="opneoutandsave(3)" ></u-button>
								</u-col>
								<u-col span="3">
								    <u-button plain style="width: 80%;" type="" text="升级" ></u-button>
								</u-col>
						    </u-row>
						</view>
					</view>
				</view>
				
			</view>
		</view>
		
		
					
<!-- 各种弹出层============================================================ -->
					<u-popup  class="popup" :show="show1" :round="10" mode="center"    @close="close" @open="open">
					        <view   style="padding: 10%;width: 80%;">
					            <u--text style="margin-top: -8%;" align="center" type="info" size="12" :text="mig"></u--text>
					        </view>
					</u-popup>
					<u-toast ref="uToast2"></u-toast>
			<!-- 存取的弹出层 -->
			        <u-modal :closeOnClickOverlay="true" :showConfirmButton="false"  @close="showoutandsave=false" :show="showoutandsave" title="存取页面" :content='content'>
					    <view style="display: block;width: 120%;">
							<u--text size="16" align="center" type="info" :text="'剩余金额(单位元)： '+balance"></u--text>
							<view style="padding-top: 5%;">
								<u--input
								    type="number"
								    placeholder="请输入金额"
								    border="surround"
								    v-model="saveorout"
								    @change="change"
								></u--input>
							</view>
							<view style="display: flex;padding-top: 2%;">
								<u-button style="flex: 2;"  type="warning" plain text="取钱" @click="outorsave(2)" ></u-button>
								<text v-if="current==0" style="flex: 1;"></text>
								<u-button v-if="current==0" style="flex: 2;" type="success" plain text="存钱" @click="outorsave(1)" ></u-button>
							</view>
						</view>
						<u-toast ref="uToastout"></u-toast>
					</u-modal>
			<!-- 开户的弹出层 -->		
				    <u-popup class="popup2" :show="show2" :round="5" mode="bottom"   @close="close2" @open="open2">
							<u-toast ref="uToast"></u-toast>
							<view  style="padding: 5%;width: 90%;">
				                <view>
									    <u--text bold text="信息验证"></u--text>
				                		<!-- 注意，如果需要兼容微信小程序，最好通过setRules方法设置rules规则 -->
				                		<u--form
				                				labelPosition="left"
				                				:model="model"
				                				ref="uForm"
				                		>
				                			<u-form-item
				                					label="姓名"
				                					prop="userInfo.name"
				                					borderBottom
													
				                					ref="item1"
				                			>
				                				<u--input
												border="bottom"
				                						v-model="model.userInfo.name"
				                				></u--input>
				                			</u-form-item>
											<u-form-item
													label="身份证号码"
													prop="userInfo.idnumber"
													borderBottom
													ref="item1"
											>
												<u--input 
														v-model="model.userInfo.idnumber"
												></u--input>
											</u-form-item>
											<u-form-item
													label="电话号码"
													prop="mobile"
													borderBottom
													ref="item1"
											>
												<u--input
												        type="number"
														v-model="model.userInfo.phone"
												>
												<template slot="suffix">
																	<u-code
																		ref="uCode"
																		@change="codeChange"
																		seconds="60"
																		changeText="X秒重新获取"
																	></u-code>
																	<u-button
																		@tap="getCode"
																		:text="tips"
																		type="success"
																		size="mini"
																	></u-button>
																</template>
												</u--input>
											</u-form-item>
											<u-form-item label="验证码" ref="item1" v-if="flag==true">
												<u-code-input @finish="finish" style="margin: 0 auto;" mode="line" v-model="codewords" :maxlength="6"></u-code-input>
											</u-form-item>
				                		</u--form>
				                	</view>
				            </view>
				    </u-popup>	
					<u-modal @confirm="createCard(type)" @cancel="cancel" :show="show3" :title="title" content="是否确认开通？开通后注销账户需到线下柜台处持身份证及相应证件办理" showCancelButton="true" ></u-modal>
				
		
	</view>
>>>>>>> df5742f475218251bd4c1536b5da73f88340e071
		
</template>

<script>
	export default {
		data() {
<<<<<<< HEAD
			return {
				user:uni.getStorageSync('user'),
				list: ['储蓄卡', '信用卡'],
				current: 0,
				show1:false,
				show2:false,
				show3:false,
				pageHeight:0,
				mig:'',
				model:{userInfo:{
					name:"",
					idnumber:"",
					phone:""
				}},
				type:'',
				tips: '',
				codewords:'',
				flag:false,
				mycards:[],
				cards1:false,
				cards2:false,
				cards3:false,
				cardsText:['','',''],
				mycardsnow:{},
				showoutandsave:false,
				balance:'',
				saveorout:'',
				cardsId:'',
				screenHeight:'',
			};
		},
		onLoad(){
			this.screenHeight = uni.getSystemInfoSync().windowHeight;
			this.getCards()
		},
		methods:{
			sectionChange(index) {//选择储蓄或信用卡
							this.current = index;
						},
		    openCreateCard(val){//打开开户面板
				this.type=val
				this.show2=true
			},
			getCards(){//获取卡号以及银行卡类型等元素的操作
				this.request({
					url:"/BankCard/getCards",
					method:"POST",
					data:{
						uid:uni.getStorageSync('user').id,
						cards:uni.getStorageSync('user').bankcards
					}
				}).then(res=>{
					if(res.code==='200'){
						this.mycards=res.data
						for(let i=0;i<this.mycards.length;i++){
							if(this.mycards[i].type===1){
								this.cards1=true
								this.cardsText[0]=(this.mycards[i].cardnumber).slice(-4)
							}
							if(this.mycards[i].type===2){
								this.cards2=true
								this.cardsText[1]=(this.mycards[i].cardnumber).slice(-4)
							}
							if(this.mycards[i].type===3){
								this.cards3=true
								this.cardsText[2]=(this.mycards[i].cardnumber).slice(-4)
							}
						}
					}
				})
				return Promise.resolve()
			},
			cancel(){//取消开户
				this.show3=false
			},
			finish(){//完成验证码输入（达到6位自动触发）
				this.request({
					url:"/Code/checkCode",
					method:"POST",
					data:{
						phoneCode:this.codewords,
						uid:uni.getStorageSync('user').id
					}
				}).then(res=>{
					this.codewords=''
					if(res.code==='200'){
						this.$refs.uToast2.show({
											type:'success',
											duration:'800',
											message:"验证成功！",
										})
						 this.flag=false
						 this.show2=false
						 this.show3=true
					}
					else if(res.code==='404'){
						this.$refs.uToast.show({
											type:'error',
											duration:'800',
											message:"验证码错误！",
										})
					}
					else if(res.code==='300'){
						this.$refs.uToast.show({
											type:'warn',
											duration:'800',
											message:"验证码已失效！请重新发送",
										})
					}
					
				})},
			 close() {
			        this.show1 = false
			        // console.log('close');
			      },
			close2() {//关闭开户信息验证面板
			       this.show2 = false
			       // console.log('close');
			     },
			createCard(val){//开户
				uni.showLoading({
				  title: '正在开通账户！请稍后'
				})
				this.request({
					url:"/BankCard/creatBankcards",
					method:"POST",
					data:{
						type:val,
						uid:uni.getStorageSync('user').id
					}
				}).then(res=>{
					uni.hideLoading();
					if(res.code==='200'){
						this.$refs.uToast2.show({
											type:'success',
											duration:'800',
											message:"账户开通成功！",
										})
						this.show3=false
						this.getCards()
					}
					else{
						this.$refs.uToast2.show({
											type:'error',
											duration:'800',
											message:"账户开通失败！详情请咨询客服",
										})
					}
				})
				
			},
			tip(val){//设置详情提示的内容
				if(val==1){
					this.mig="龙卡通账户为电子Ⅰ类账户,每个客户只能在本银行办理一张龙卡通,该卡可以用于消费.转账.购买理财产品等操作,没有明确的额度限制"
					this.show1 = true
				}
				else if(val==2){
					this.mig="个人养老金账户为电子Ⅱ类账户，是我行为个人客户提供的个人养老金专用资金账户，具有资金存入、查询资金、待遇领取等功能"
					this.show1=true
				}
			},
			onReady() {
					//如果需要兼容微信小程序，并且校验规则中含有方法等，只能通过setRules方法设置规则。
			    	this.$refs.uForm.setRules(this.rules)},
			detail(val){//明细
			    this.getCards().then(e=>{
					for(let i=0;i<this.mycards.length;i++){
						if(this.mycards[i].type===val){
							console.log(this.mycards[i])
							uni.setStorageSync('bankdetail',this.mycards[i])
							break
					    }
					}
				}).then(e=>{
					this.toDetail()
				});
					
				
			},
			toDetail(){//跳转到明细页面
				uni.navigateTo({
					url:"/pages/bankcards/detail/detail"
				})
			},
			codeChange(text) {//验证码输入
                     this.tips = text;
                   },
            getCode() {//发送验证码前的验证
	              if(this.model.userInfo.phone!=this.user.phone){
	              	uni.$u.toast('电话号码错误！');
	              }
	              else if(this.model.userInfo.name!=this.user.name||this.model.userInfo.idnumber!=this.user.idcard){
	              	uni.$u.toast('存在信息错误！');
	              }
	              else{
	              	if (this.$refs.uCode.canGetCode) {
	              	  // 模拟向后端请求验证码
	              	  
	              	  this.sentCode();
	              	} else {
	              	  uni.$u.toast('倒计时结束后再发送');
	              	}
	              }},
	        sentCode(){//发送验证码
			      uni.showLoading({
			        title: '正在获取验证码'
			      })
			      this.request({
			      	url:"/Code/setCode",
			      	method:"POST",
			      	data:{
			      		uid:uni.getStorageSync('user').id,
			      		phone:this.model.userInfo.phone
			      	}
			      }).then(res=>{
			      	uni.hideLoading();
			      	// 这里此提示会被this.start()方法中的提示覆盖
			      	if(res.code==='200'){
			      		uni.$u.toast('验证码已发送,5分钟内有效');
			      		// 通知验证码组件内部开始倒计时
			      		this.$refs.uCode.start();
			      		this.flag=true
			      	}
			      	else{
			      		uni.$u.toast('验证码发送失败！请稍后再试');
			      	}
		       })}, 
			opneoutandsave(val){//打开存取页面
				   for(let i=0;i<this.mycards.length;i++){
				   	if(this.mycards[i].type==val){
				   		this.balance=this.mycards[i].balance
						this.cardsId=this.mycards[i].id
				   		this.showoutandsave=true
				   		break
				   	}
				   }
			 },
			   
		    outorsave(val){//取钱
			    let surl=""
			    if(val===1){//存钱
					surl="/Pay/save"
				}
				else if(val===2){//取钱
					surl="/Pay/out"
					if(this.saveorout>this.balance){
						this.$refs.uToastout.show({
											type:'error',
											duration:'1200',
											message:"请输入比余额小的数值！",
										})
						this.saveorout=''
						return false
					}
				}
				if(this.saveorout>0){
					this.request({
						url:surl,
						method:"POST",
						data:{
							cardid:this.cardsId,
							cost:this.saveorout,
						}
					}).then(res=>{
						if(res.code==='200'){
							this.showoutandsave=false
							this.saveorout=''
							this.$refs.uToast2.show({
												type:'success',
												duration:'1200',
												message:"操作成功！",
											})
						}
						this.getCards()
					})	
				}
				else{
					this.$refs.uToastout.show({
										type:'error',
										duration:'1200',
										message:"请输入比0大的数值！",
									})
				    this.saveorout=''
				}
			},
			toTransfer(val){//转移到转账页面
			    this.getCards().then(e=>{
			    	for(let i=0;i<this.mycards.length;i++){
			    		if(this.mycards[i].type===val){
			    			console.log(this.mycards[i])
			    			uni.setStorageSync('bankdetail',this.mycards[i])
			    			break
			    	    }
			    	}
			    }).then(e=>{
			    	uni.navigateTo({
			    		url:"/pages/bankcards/transfer/transfer"
			    	})
			    });
			}
			
		}
	        
			
=======
			return {
				user:uni.getStorageSync('user'),
				list: ['储蓄卡', '信用卡'],
				current: 0,
				show1:false,
				show2:false,
				show3:false,
				pageHeight:0,
				mig:'',
				model:{userInfo:{
					name:"",
					idnumber:"",
					phone:""
				}},
				type:'',
				tips: '',
				codewords:'',
				flag:false,
				mycards:[],
				cards1:false,
				cards2:false,
				cards3:false,
				cardsText:['','',''],
				mycardsnow:{},
				showoutandsave:false,
				balance:'',
				saveorout:'',
				cardsId:'',
				screenHeight:'',
			};
		},
		onLoad(){
			this.screenHeight = uni.getSystemInfoSync().windowHeight+"px";
			this.getCards()
		},
		methods:{
			sectionChange(index) {//选择储蓄或信用卡
							this.current = index;
						},
		    openCreateCard(val){//打开开户面板
				this.type=val
				this.show2=true
			},
			getCards(){//获取卡号以及银行卡类型等元素的操作
				this.request({
					url:"/BankCard/getCards",
					method:"POST",
					data:{
						uid:uni.getStorageSync('user').id,
						cards:uni.getStorageSync('user').bankcards
					}
				}).then(res=>{
					if(res.code==='200'){
						this.mycards=res.data
						for(let i=0;i<this.mycards.length;i++){
							if(this.mycards[i].type===1){
								this.cards1=true
								this.cardsText[0]=(this.mycards[i].cardnumber).slice(-4)
							}
							if(this.mycards[i].type===2){
								this.cards2=true
								this.cardsText[1]=(this.mycards[i].cardnumber).slice(-4)
							}
							if(this.mycards[i].type===3){
								this.cards3=true
								this.cardsText[2]=(this.mycards[i].cardnumber).slice(-4)
							}
						}
					}
				})
				return Promise.resolve()
			},
			cancel(){//取消开户
				this.show3=false
			},
			finish(){//完成验证码输入（达到6位自动触发）
				this.request({
					url:"/Code/checkCode",
					method:"POST",
					data:{
						phoneCode:this.codewords,
						uid:uni.getStorageSync('user').id
					}
				}).then(res=>{
					this.codewords=''
					if(res.code==='200'){
						this.$refs.uToast2.show({
											type:'success',
											duration:'800',
											message:"验证成功！",
										})
						 this.flag=false
						 this.show2=false
						 this.show3=true
					}
					else if(res.code==='404'){
						this.$refs.uToast.show({
											type:'error',
											duration:'800',
											message:"验证码错误！",
										})
					}
					else if(res.code==='300'){
						this.$refs.uToast.show({
											type:'warn',
											duration:'800',
											message:"验证码已失效！请重新发送",
										})
					}
					
				})},
			 close() {
			        this.show1 = false
			        // console.log('close');
			      },
			close2() {//关闭开户信息验证面板
			       this.show2 = false
			       // console.log('close');
			     },
			createCard(val){//开户
				uni.showLoading({
				  title: '正在开通账户！请稍后'
				})
				this.request({
					url:"/BankCard/creatBankcards",
					method:"POST",
					data:{
						type:val,
						uid:uni.getStorageSync('user').id
					}
				}).then(res=>{
					uni.hideLoading();
					if(res.code==='200'){
						this.$refs.uToast2.show({
											type:'success',
											duration:'800',
											message:"账户开通成功！",
										})
						this.show3=false
						this.getCards()
					}
					else{
						this.$refs.uToast2.show({
											type:'error',
											duration:'800',
											message:"账户开通失败！详情请咨询客服",
										})
					}
				})
				
			},
			tip(val){//设置详情提示的内容
				if(val==1){
					this.mig="龙卡通账户为电子Ⅰ类账户,每个客户只能在本银行办理一张龙卡通,该卡可以用于消费.转账.购买理财产品等操作,没有明确的额度限制"
					this.show1 = true
				}
				else if(val==2){
					this.mig="个人养老金账户为电子Ⅱ类账户，是我行为个人客户提供的个人养老金专用资金账户，具有资金存入、查询资金、待遇领取等功能"
					this.show1=true
				}
				else if(val==3){
					this.mig="信用卡为电子Ⅱ类账户，是我行为个人客户提供的个人养老金专用资金账户，具有资金存入、查询资金、待遇领取等功能"
					this.show1=true
				}
			},
			onReady() {
					//如果需要兼容微信小程序，并且校验规则中含有方法等，只能通过setRules方法设置规则。
			    	this.$refs.uForm.setRules(this.rules)},
			detail(val){//明细
			    console.log("val"+val)
			    this.getCards().then(e=>{
					for(let i=0;i<this.mycards.length;i++){
						if(this.mycards[i].type===val){
							console.log(this.mycards[i])
							uni.setStorageSync('bankdetail',this.mycards[i])
							break
					    }
					}
				}).then(e=>{
					this.toDetail()
				});
					
				
			},
			toDetail(){//跳转到明细页面
				uni.navigateTo({
					url:"/pages/bankcards/detail/detail"
				})
			},
			returnm(){//跳转到还款
				this.getCards().then(e=>{
					for(let i=0;i<this.mycards.length;i++){
						if(this.mycards[i].type===3){
							console.log(this.mycards[i])
							uni.setStorageSync('bankdetail',this.mycards[i])
							break
					    }
					}
				}).then(e=>{
					uni.navigateTo({
						url:"/pages/bankcards/returncredit/returncredit"
					})
				});
				
			},
			codeChange(text) {//验证码输入
                     this.tips = text;
                   },
            getCode() {//发送验证码前的验证
	              if(this.model.userInfo.phone!=this.user.phone){
	              	uni.$u.toast('电话号码错误！');
	              }
	              else if(this.model.userInfo.name!=this.user.name||this.model.userInfo.idnumber!=this.user.idcard){
	              	uni.$u.toast('存在信息错误！');
	              }
	              else{
	              	if (this.$refs.uCode.canGetCode) {
	              	  // 模拟向后端请求验证码
	              	  
	              	  this.sentCode();
	              	} else {
	              	  uni.$u.toast('倒计时结束后再发送');
	              	}
	              }},
	        sentCode(){//发送验证码
			      uni.showLoading({
			        title: '正在获取验证码'
			      })
			      this.request({
			      	url:"/Code/setCode",
			      	method:"POST",
			      	data:{
			      		uid:uni.getStorageSync('user').id,
			      		phone:this.model.userInfo.phone
			      	}
			      }).then(res=>{
			      	uni.hideLoading();
			      	// 这里此提示会被this.start()方法中的提示覆盖
			      	if(res.code==='200'){
			      		uni.$u.toast('验证码已发送,5分钟内有效');
			      		// 通知验证码组件内部开始倒计时
			      		this.$refs.uCode.start();
			      		this.flag=true
			      	}
			      	else{
			      		uni.$u.toast('验证码发送失败！请稍后再试');
			      	}
		       })}, 
			opneoutandsave(val){//打开存取页面
				   for(let i=0;i<this.mycards.length;i++){
				   	if(this.mycards[i].type==val){
				   		this.balance=this.mycards[i].balance
						this.cardsId=this.mycards[i].id
				   		this.showoutandsave=true
				   		break
				   	}
				   }
			 },
			   
		    outorsave(val){//取钱
			    let surl=""
			    if(val===1){//存钱
					surl="/Pay/save"
				}
				else if(val===2){//取钱
					surl="/Pay/out"
					if(this.saveorout>this.balance){
						this.$refs.uToastout.show({
											type:'error',
											duration:'1200',
											message:"请输入比余额小的数值！",
										})
						this.saveorout=''
						return false
					}
				}
				if(this.saveorout>0){
					this.request({
						url:surl,
						method:"POST",
						data:{
							cardid:this.cardsId,
							cost:this.saveorout,
						}
					}).then(res=>{
						if(res.code==='200'){
							this.showoutandsave=false
							this.saveorout=''
							this.$refs.uToast2.show({
												type:'success',
												duration:'1200',
												message:"操作成功！",
											})
						}
						this.getCards()
					})	
				}
				else{
					this.$refs.uToastout.show({
										type:'error',
										duration:'1200',
										message:"请输入比0大的数值！",
									})
				    this.saveorout=''
				}
			},
			toTransfer(val){//转移到转账页面
			    this.getCards().then(e=>{
			    	for(let i=0;i<this.mycards.length;i++){
			    		if(this.mycards[i].type===val){
			    			console.log(this.mycards[i])
			    			uni.setStorageSync('bankdetail',this.mycards[i])
			    			break
			    	    }
			    	}
			    }).then(e=>{
			    	uni.navigateTo({
			    		url:"/pages/bankcards/transfer/transfer"
			    	})
			    });
			},
			
			
		}
	        
			
>>>>>>> df5742f475218251bd4c1536b5da73f88340e071
	}
</script>

<style lang="scss">
<<<<<<< HEAD
.subsection{
	width: 80%;
	margin: 2% auto;
}
.box{
	margin: 5% auto;
	width: 95%;
	background-color: #fff;
	border: 1px solid #ddd;
	border-radius: 5px;
	box-shadow: 0 2px 5px rgba(0,0,0,0.1);
	background-image: url(~@/static/image/inset/background3.jpg);
	background-size: 100% 100%;
	background-position: 50% 50%;
	background-repeat: no-repeat;
}
.box1{
	margin: 5% auto;
	width: 95%;
	background-color: #fff;
	border: 1px solid #ddd;
	border-radius: 5px;
	box-shadow: 0 2px 5px rgba(0,0,0,0.1);
	background-image: url(~@/static/image/inset/bankCard1.png);
	background-size: 25% 100%;
	background-position: 0 -20rpx;
	background-repeat: no-repeat;
}
.box2{
	margin: 5% auto;
	width: 95%;
	background-color: #fff;
	border: 1px solid #ddd;
	border-radius: 5px;
	box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}
.popup{
	width: 80%;
}
.popup2{
	width: 100%;
}
=======
.subsection{
	width: 80%;
	margin: 2% auto;
}
.background1{
	position: relative;
	background-image: url(~@/static/image/inset/plant.png);
	background-size: 100% 100%;
	background-repeat: no-repeat;
	width: 100vw;
}
.background2{
	position: relative;
	background-image: url(~@/static/image/inset/bankCard3.png);
	background-size: 100% 60%;
	background-repeat: no-repeat;
	background-position: 0 30%;
	width: 100vw;
}
.box{
	margin: 5% auto;
	width: 95%;
	background-color: #fff;
	border: 1px solid #ddd;
	border-radius: 5px;
	box-shadow: 0 2px 5px rgba(0,0,0,0.1);
	background-image: url(~@/static/image/inset/background3.jpg);
	background-size: 100% 100%;
	background-position: 50% 50%;
	background-repeat: no-repeat;
}
.box4{
	margin: 5% auto;
	width: 95%;
	background-color: #fff;
	border: 1px solid #ddd;
	border-radius: 5px;
	box-shadow: 0 2px 5px rgba(0,0,0,0.1);
	background-image: url(~@/static/image/inset/background5.jpg);
	background-size: 100% 100%;
	background-position: 50% 50%;
	background-repeat: no-repeat;
}
.box1{
	margin: 5% auto;
	width: 95%;
	background-color: #fff;
	border: 1px solid #ddd;
	border-radius: 5px;
	box-shadow: 0 2px 5px rgba(0,0,0,0.1);
	background-image: url(~@/static/image/inset/bankCard1.png);
	background-size: 25% 100%;
	background-position: 0 -20rpx;
	background-repeat: no-repeat;
}
.box2{
	margin: 5% auto;
	width: 95%;
	background-color: #fff;
	border: 1px solid #ddd;
	border-radius: 5px;
	box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}
.box3{
	margin: 5% auto;
	width: 95%;
	background-color: #fff;
	border: 1px solid #ddd;
	border-radius: 5px;
	box-shadow: 0 2px 5px rgba(0,0,0,0.1);
	background-image: url(~@/static/image/inset/bankCard.png);
	background-size: 15% 45%;
	background-position: 19rpx 12rpx;
	background-repeat: no-repeat;
}
.popup{
	width: 80%;
}
.popup2{
	width: 100%;
}
>>>>>>> df5742f475218251bd4c1536b5da73f88340e071

</style>
