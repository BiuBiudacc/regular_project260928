## 	批量入住床位接口

**需求描述:**本接口为学生管理的入住设置的报错接口,接口支持批量为学生和床位进行关系绑定以此完成入住.

**接口地址**:`/api/bedStudentRel/batchCheckIn`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:<p>接收多个学生和床位关系以及共有的入住区间信息</p>



**请求示例**:


```javascript
{
  "itemList": [
    {
      "studentId": 0,
      "bedId": 0
    }
  ],
  "checkInDate": "",
  "estimateCheckOutDate": ""
}
```


**请求参数**:


**请求参数**:


| 参数名称                          | 参数说明           | 请求类型 | 是否必须 | 数据类型                  | schema                    |
| --------------------------------- | ------------------ | -------- | -------- | ------------------------- | ------------------------- |
| studentBedBatchCheckInDTO         | 批量学生入住DTO    | body     | true     | StudentBedBatchCheckInDTO | StudentBedBatchCheckInDTO |
| &emsp;&emsp;itemList              | 单条学生床位分配项 |          | true     | array                     | StudentBedItemDTO         |
| &emsp;&emsp;&emsp;&emsp;studentId | 学生id             |          | true     | integer                   |                           |
| &emsp;&emsp;&emsp;&emsp;bedId     | 床位id             |          | true     | integer                   |                           |
| &emsp;&emsp;checkInDate           | 入住日期           |          | true     | string(date-time)         |                           |
| &emsp;&emsp;estimateCheckOutDate  | 预估退宿日期       |          | true     | string(date-time)         |                           |


**响应状态**:


| 状态码 | 说明 | schema     |
| ------ | ---- | ---------- |
| 200    | OK   | ResultVoid |


**响应参数**:


| 参数名称 | 参数说明                | 类型           | schema         |
| -------- | ----------------------- | -------------- | -------------- |
| code     | 业务响应码，200代表成功 | integer(int32) | integer(int32) |
| msg      | 返回消息提示            | string         |                |
| data     | 数据主体                | object         |                |


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {}
}
```

## 入住记录查询接口

需求描述:本接口用于查询选中的学生的入住记录

**接口地址**:`/api/bedStudentRel/studentRecord`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>根据学生id查询入住记录</p>



**请求参数**:


**请求参数**:


| 参数名称              | 参数说明 | 请求类型 | 是否必须 | 数据类型                 | schema                   |
| --------------------- | -------- | -------- | -------- | ------------------------ | ------------------------ |
| dto                   |          | query    | true     | StudentBedRecordQueryDTO | StudentBedRecordQueryDTO |
| &emsp;&emsp;studentId | 学生id   |          | true     | integer(int64)           |                          |


**响应状态**:


| 状态码 | 说明 | schema                       |
| ------ | ---- | ---------------------------- |
| 200    | OK   | ResultListStudentBedRecordVO |


**响应参数**:


| 参数名称                      | 参数说明                        | 类型              | schema             |
| ----------------------------- | ------------------------------- | ----------------- | ------------------ |
| code                          | 业务响应码，200代表成功         | integer(int32)    | integer(int32)     |
| msg                           | 返回消息提示                    | string            |                    |
| data                          | 数据主体                        | array             | StudentBedRecordVO |
| &emsp;&emsp;roomId            | 房间id                          | integer(int64)    |                    |
| &emsp;&emsp;roomName          | 房间名称                        | string            |                    |
| &emsp;&emsp;bedId             | 床位id                          | integer(int64)    |                    |
| &emsp;&emsp;bedName           | 床位名称                        | string            |                    |
| &emsp;&emsp;checkInDate       | 入住日期                        | string(date-time) |                    |
| &emsp;&emsp;estimateLeaveTime | 离开日期                        | string(date-time) |                    |
| &emsp;&emsp;realLeaveTime     | 实际离开日期                    | string(date-time) |                    |
| &emsp;&emsp;status            | 入住状态,正在入住/待入住/已结束 | string            |                    |


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"roomId": 1,
			"roomName": "",
			"bedId": 0,
			"bedName": "",
			"checkInDate": "",
			"estimateLeaveTime": "",
			"realLeaveTime": "",
			"status": ""
		}
	]
}
```

## 入住记录删除接口


**接口地址**:`/api/bedStudentRel/deleteRelRecord`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>接收入入住记录id,删除入住记录</p>



**请求示例**:


```javascript
{
  "id": 0
}
```


**请求参数**:


**请求参数**:


| 参数名称        | 参数说明        | 请求类型 | 是否必须 | 数据类型        | schema          |
| --------------- | --------------- | -------- | -------- | --------------- | --------------- |
| bedRelDeleteDTO | BedRelDeleteDTO | body     | true     | BedRelDeleteDTO | BedRelDeleteDTO |
| &emsp;&emsp;id  | 入住记录id      |          | true     | integer(int64)  |                 |


**响应状态**:


| 状态码 | 说明 | schema     |
| ------ | ---- | ---------- |
| 200    | OK   | ResultVoid |


**响应参数**:


| 参数名称 | 参数说明                | 类型           | schema         |
| -------- | ----------------------- | -------------- | -------------- |
| code     | 业务响应码，200代表成功 | integer(int32) | integer(int32) |
| msg      | 返回消息提示            | string         |                |
| data     | 数据主体                | object         |                |


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {}
}
```

## 学生信息接口,支持分页

**需求描述:**该功能已完成,需要你根据返回的warnStatus字段进行提示,如果1和2需要警告提醒,其他不管,1提示今日到期,2提示已超期

**接口地址**:`/api/student/page`

**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>返回学生列表信息</p>



**请求示例**:


```javascript
{
  "pageNum": 0,
  "pageSize": 0,
  "studentName": "",
  "batchId": 0,
  "checkInStatus": "字符串,已入住,未入住"
}
```


**请求参数**:


**请求参数**:


| 参数名称                  | 参数说明        | 请求类型 | 是否必须 | 数据类型        | schema          |
| ------------------------- | --------------- | -------- | -------- | --------------- | --------------- |
| studentQueryDTO           | StudentQueryDTO | body     | true     | StudentQueryDTO | StudentQueryDTO |
| &emsp;&emsp;pageNum       | 页码            |          | false    | integer(int64)  |                 |
| &emsp;&emsp;pageSize      | 每页条数        |          | false    | integer(int64)  |                 |
| &emsp;&emsp;studentName   | 学生名称        |          | false    | string          |                 |
| &emsp;&emsp;batchId       | 批次id          |          | false    | integer(int64)  |                 |
| &emsp;&emsp;checkInStatus | 入住状态        |          | false    | string          |                 |


**响应状态**:


| 状态码 | 说明 | schema                    |
| ------ | ---- | ------------------------- |
| 200    | OK   | ResultPageVOStudentPageVO |


**响应参数**:


| 参数名称                                      | 参数说明                                            | 类型                | schema              |
| --------------------------------------------- | --------------------------------------------------- | ------------------- | ------------------- |
| code                                          | 业务响应码，200代表成功                             | integer(int32)      | integer(int32)      |
| msg                                           | 返回消息提示                                        | string              |                     |
| data                                          |                                                     | PageVOStudentPageVO | PageVOStudentPageVO |
| &emsp;&emsp;current                           | 当前页码                                            | integer(int64)      |                     |
| &emsp;&emsp;size                              | 每页条数                                            | integer(int64)      |                     |
| &emsp;&emsp;total                             | 总记录数                                            | integer(int64)      |                     |
| &emsp;&emsp;pages                             | 总页数                                              | integer(int64)      |                     |
| &emsp;&emsp;records                           | 当前页数据列表                                      | array               | StudentPageVO       |
| &emsp;&emsp;&emsp;&emsp;id                    | 学生id                                              | integer             |                     |
| &emsp;&emsp;&emsp;&emsp;studentName           | 学生姓名                                            | string              |                     |
| &emsp;&emsp;&emsp;&emsp;batchId               | 批次id                                              | integer             |                     |
| &emsp;&emsp;&emsp;&emsp;batchName             | 批次名称                                            | string              |                     |
| &emsp;&emsp;&emsp;&emsp;studentGender         | 学生性别                                            | integer             |                     |
| &emsp;&emsp;&emsp;&emsp;studentAge            | 学生年龄                                            | integer             |                     |
| &emsp;&emsp;&emsp;&emsp;className             | 班级id                                              | string              |                     |
| &emsp;&emsp;&emsp;&emsp;studentPhone          | 联系方式                                            | string              |                     |
| &emsp;&emsp;&emsp;&emsp;idCard                | 身份证号码                                          | string              |                     |
| &emsp;&emsp;&emsp;&emsp;emergencyContactName  | 学生紧急联系人                                      | string              |                     |
| &emsp;&emsp;&emsp;&emsp;emergencyContactPhone | 学生紧急联系人电话                                  | string              |                     |
| &emsp;&emsp;&emsp;&emsp;classId               | 所属班级id                                          | integer             |                     |
| &emsp;&emsp;&emsp;&emsp;roomId                | 宿舍id                                              | integer             |                     |
| &emsp;&emsp;&emsp;&emsp;roomName              | 宿舍名称                                            | string              |                     |
| &emsp;&emsp;&emsp;&emsp;bedId                 | 床位id                                              | integer             |                     |
| &emsp;&emsp;&emsp;&emsp;bedName               | 床位名称                                            | string              |                     |
| &emsp;&emsp;&emsp;&emsp;checkInStatus         | 入住状态                                            | string              |                     |
| &emsp;&emsp;&emsp;&emsp;warnStatus            | 到期退房提醒,1代表今日到期,2代表未到期,3代表已超期, | string              |                     |
| &emsp;&emsp;&emsp;&emsp;estimateLeaveTime     | 预计离开日期                                        | string              |                     |


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"current": 1,
		"size": 10,
		"total": 56,
		"pages": 6,
		"records": [
			{
				"id": 1,
				"studentName": "1",
				"batchId": 1,
				"batchName": "1",
				"studentGender": 1,
				"studentAge": 0,
				"className": "1",
				"studentPhone": "1",
				"idCard": "",
				"emergencyContactName": "",
				"emergencyContactPhone": "",
				"classId": 0,
				"roomId": 1,
				"roomName": "1",
				"bedId": 1,
				"bedName": "1",
				"checkInStatus": "已入住/未入住",
				"warnStatus": "",
				"estimateLeaveTime": ""
			}
		]
	}
}
```

## 退房接口


**接口地址**:`/api/bedStudentRel/batchCheckOut`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>接收学生id集合以及退房时间</p>



**请求示例**:


```javascript
{
  "studentIdList": [],
  "checkOutDate": ""
}
```


**请求参数**:


**请求参数**:


| 参数名称                  | 参数说明               | 请求类型 | 是否必须 | 数据类型           | schema             |
| ------------------------- | ---------------------- | -------- | -------- | ------------------ | ------------------ |
| studentCheckOutDTO        | 批量退房DTO            | body     | true     | StudentCheckOutDTO | StudentCheckOutDTO |
| &emsp;&emsp;studentIdList | 学生id集合             |          | true     | array              | integer(int64)     |
| &emsp;&emsp;checkOutDate  | 退房日期，不传默认当天 |          | false    | string(date)       |                    |


**响应状态**:


| 状态码 | 说明 | schema     |
| ------ | ---- | ---------- |
| 200    | OK   | ResultVoid |


**响应参数**:


| 参数名称 | 参数说明                | 类型           | schema         |
| -------- | ----------------------- | -------------- | -------------- |
| code     | 业务响应码，200代表成功 | integer(int32) | integer(int32) |
| msg      | 返回消息提示            | string         |                |
| data     | 数据主体                | object         |                |


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {}
}
```

## 学生当前入住信息查询接口


**接口地址**:`/api/bedStudentRel/getCurrentCheckIn`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>查询指定学生当前的入住信息</p>



**请求参数**:


**请求参数**:


| 参数名称              | 参数说明 | 请求类型 | 是否必须 | 数据类型                  | schema                    |
| --------------------- | -------- | -------- | -------- | ------------------------- | ------------------------- |
| dto                   |          | query    | true     | StudentCurrentBedQueryDTO | StudentCurrentBedQueryDTO |
| &emsp;&emsp;studentId | 学生id   |          | true     | integer(int64)            |                           |


**响应状态**:


| 状态码 | 说明 | schema                    |
| ------ | ---- | ------------------------- |
| 200    | OK   | ResultStudentCurrentBedVO |


**响应参数**:


| 参数名称                      | 参数说明                | 类型                | schema              |
| ----------------------------- | ----------------------- | ------------------- | ------------------- |
| code                          | 业务响应码，200代表成功 | integer(int32)      | integer(int32)      |
| msg                           | 返回消息提示            | string              |                     |
| data                          |                         | StudentCurrentBedVO | StudentCurrentBedVO |
| &emsp;&emsp;roomId            | 房间id                  | integer(int64)      |                     |
| &emsp;&emsp;roomName          | 房间名称                | string              |                     |
| &emsp;&emsp;bedId             | 床位id                  | integer(int64)      |                     |
| &emsp;&emsp;bedName           | 床位名称                | string              |                     |
| &emsp;&emsp;checkInDate       | 入住时间                | string(date-time)   |                     |
| &emsp;&emsp;estimateLeaveDate | 预计退房时间            | string(date-time)   |                     |


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"roomId": 0,
		"roomName": "",
		"bedId": 0,
		"bedName": "",
		"checkInDate": "",
		"estimateLeaveDate": ""
	}
}
```

## 学生续住接口（同床位延长入住时间）


**接口地址**:`/api/bedStudentRel/renew`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>校验床位在延长区间无其他入住记录，更新预计退宿时间</p>



**请求示例**:


```javascript
{
  "studentId": 1,
  "bedRelId": 10,
  "bedId": 5,
  "oldEstimateLeaveDate": "",
  "newEstimateLeaveDate": ""
}
```


**请求参数**:


**请求参数**:


| 参数名称                         | 参数说明                         | 请求类型 | 是否必须 | 数据类型          | schema          |
| -------------------------------- | -------------------------------- | -------- | -------- | ----------------- | --------------- |
| studentRenewDTO                  | StudentRenewDTO                  | body     | true     | StudentRenewDTO   | StudentRenewDTO |
| &emsp;&emsp;studentId            | 学生id                           |          | true     | integer(int64)    |                 |
| &emsp;&emsp;bedRelId             | 床位学生关联主键id               |          | true     | integer(int64)    |                 |
| &emsp;&emsp;bedId                | 床位id                           |          | true     | integer(int64)    |                 |
| &emsp;&emsp;oldEstimateLeaveDate | 原预计离开日期（用于乐观锁校验） |          | true     | string(date-time) |                 |
| &emsp;&emsp;newEstimateLeaveDate | 续住新的预计离开日期             |          | true     | string(date-time) |                 |


**响应状态**:


| 状态码 | 说明 | schema     |
| ------ | ---- | ---------- |
| 200    | OK   | ResultVoid |


**响应参数**:


| 参数名称 | 参数说明                | 类型           | schema         |
| -------- | ----------------------- | -------------- | -------------- |
| code     | 业务响应码，200代表成功 | integer(int32) | integer(int32) |
| msg      | 返回消息提示            | string         |                |
| data     | 数据主体                | object         |                |


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {}
}
```