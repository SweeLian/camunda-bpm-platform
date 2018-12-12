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

drop index ACT_IDX_HI_CAS_I_CLOSE;
drop index ACT_IDX_HI_CAS_I_BUSKEY;
drop index ACT_IDX_HI_CAS_I_TENANT_ID;
drop index ACT_IDX_HI_CAS_A_I_CREATE;
drop index ACT_IDX_HI_CAS_A_I_END;
drop index ACT_IDX_HI_CAS_A_I_COMP;
drop index ACT_IDX_HI_CAS_A_I_TENANT_ID;

drop table ACT_HI_CASEINST;
drop table ACT_HI_CASEACTINST;
