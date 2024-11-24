/*
 * Copyright 2024 LastBox
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tech.lastbox.lastshield.basicauth.repository;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import tech.lastbox.jwt.TokenEntity;
import tech.lastbox.jwt.TokenStore;

/**
 * Repository interface for handling token-related operations.
 * This interface extends JpaRepository for CRUD operations and TokenStore for token-specific behavior.
 * <p>
 * The repository is conditional on the 'lastshield.basicauth' property being set to true.
 * This means the repository will only be active when the specified property is enabled.
 */
@ConditionalOnProperty(name = "lastshield.basicauth", havingValue = "true")
@Schema(description = "Repository for handling token data and providing token-specific operations.")
public interface TokenRepository extends JpaRepository<TokenEntity, String>, TokenStore {
}
