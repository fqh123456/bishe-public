<<<<<<< HEAD
'use strict'

/**
 * Creates a new URL by combining the specified URLs
 *
 * @param {string} baseURL The base URL
 * @param {string} relativeURL The relative URL
 * @returns {string} The combined URL
 */
export default function combineURLs(baseURL, relativeURL) {
    return relativeURL
        ? `${baseURL.replace(/\/+$/, '')}/${relativeURL.replace(/^\/+/, '')}`
        : baseURL
}
=======
'use strict'

/**
 * Creates a new URL by combining the specified URLs
 *
 * @param {string} baseURL The base URL
 * @param {string} relativeURL The relative URL
 * @returns {string} The combined URL
 */
export default function combineURLs(baseURL, relativeURL) {
    return relativeURL
        ? `${baseURL.replace(/\/+$/, '')}/${relativeURL.replace(/^\/+/, '')}`
        : baseURL
}
>>>>>>> df5742f475218251bd4c1536b5da73f88340e071
