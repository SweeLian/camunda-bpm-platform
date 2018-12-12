--
-- Copyright © 2013-2018 camunda services GmbH and various authors (info@camunda.com)
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--     http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

drop index ACT_HI_DECINST.ACT_IDX_HI_DEC_INST_ID;
drop index ACT_HI_DECINST.ACT_IDX_HI_DEC_INST_KEY;
drop index ACT_HI_DECINST.ACT_IDX_HI_DEC_INST_PI;
drop index ACT_HI_DECINST.ACT_IDX_HI_DEC_INST_CI;
drop index ACT_HI_DECINST.ACT_IDX_HI_DEC_INST_ACT;
drop index ACT_HI_DECINST.ACT_IDX_HI_DEC_INST_ACT_INST;
drop index ACT_HI_DECINST.ACT_IDX_HI_DEC_INST_TIME;
drop index ACT_HI_DECINST.ACT_IDX_HI_DEC_INST_TENANT_ID;
drop index ACT_HI_DECINST.ACT_IDX_HI_DEC_INST_ROOT_ID;
drop index ACT_HI_DECINST.ACT_IDX_HI_DEC_INST_REQ_ID;
drop index ACT_HI_DECINST.ACT_IDX_HI_DEC_INST_REQ_KEY;
drop index ACT_HI_DECINST.ACT_IDX_HI_DEC_INST_ROOT_PI;
drop index ACT_HI_DECINST.ACT_IDX_HI_DEC_INST_RM_TIME;

drop index ACT_HI_DEC_IN.ACT_IDX_HI_DEC_IN_INST;
drop index ACT_HI_DEC_IN.ACT_IDX_HI_DEC_IN_CLAUSE;
drop index ACT_HI_DEC_IN.ACT_IDX_HI_DEC_IN_ROOT_PI;
drop index ACT_HI_DEC_IN.ACT_IDX_HI_DEC_IN_RM_TIME;

drop index ACT_HI_DEC_OUT.ACT_IDX_HI_DEC_OUT_INST;
drop index ACT_HI_DEC_OUT.ACT_IDX_HI_DEC_OUT_RULE;
drop index ACT_HI_DEC_OUT.ACT_IDX_HI_DEC_OUT_ROOT_PI;
drop index ACT_HI_DEC_OUT.ACT_IDX_HI_DEC_OUT_RM_TIME;

if exists (select TABLE_NAME from INFORMATION_SCHEMA.TABLES where TABLE_NAME = 'ACT_HI_DECINST') drop table ACT_HI_DECINST;
if exists (select TABLE_NAME from INFORMATION_SCHEMA.TABLES where TABLE_NAME = 'ACT_HI_DEC_IN') drop table ACT_HI_DEC_IN;
if exists (select TABLE_NAME from INFORMATION_SCHEMA.TABLES where TABLE_NAME = 'ACT_HI_DEC_OUT') drop table ACT_HI_DEC_OUT;
