// Function to fetch the compose reply toolbar
function findComposeToolBar() {
    const selectors = ['.btC', '.aDh', '[role="toolbar"]', '.gU.Up'];

    for (const selector of selectors) {
        const toolbar = document.querySelector(selector);
        if (toolbar) {
            return toolbar;
        }
    }

    return null;
}

// Function to get the email content
function getEmailContent() {
    const selectors = ['.h7', '.a3s.aiL', '.gmail_quote', '[role="presentation"]'];

    for (const selector of selectors) {
        const content = document.querySelector(selector);
        if (content) {
            return content.innerText.trim();
        }
    }

    return null;
}

// Function to create AI reply button
function createButton() {
    const button = document.createElement("div");
    button.className = "T-I J-J5-Ji aoO v7 T-I-atl L3";
    button.classList.add("mailForge-Ai");
    button.setAttribute("data-tooltip", "Generate Reply");
    button.setAttribute("role", "button");
    button.style.userSelect = "none";
    button.style.borderRadius = "20px";
    button.style.marginRight = "8px";
    button.style.padding = "0 10px";
    button.innerText = "AI Reply ";
    return button;
}

// Function to inject the AI reply button
function injectButton() {
    const existingButton = document.querySelector('.mailForge-Ai');
    if (existingButton) {
        existingButton.remove();
    }

    const composeBar = findComposeToolBar();
    if (!composeBar) {
        return;
    }

    const replyButton = createButton();

    replyButton.addEventListener("click", async () => {
        const emailContent = getEmailContent();
        const tone = "professional";

        if (!emailContent) {
            alert("No email content found.");
            return;
        }

        try {
            const response = await fetch("http://localhost:8080/generate-reply", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    email: emailContent,
                    tone: tone
                })
            });

            if (!response.ok) {
                throw new Error("API request failed");
            }

            const data = await response.json();
            const reply = data["response"];
            insertReply(reply);

        } catch (err) {
            console.error("Error calling backend:", err);
            alert("Failed to generate reply. Check console for details.");
        }
    });

    composeBar.insertBefore(replyButton, composeBar.firstChild);
}

// Updated insertReply function - insert plain text, not HTML
function insertReply(replyText) {
    console.log("Original replyText:", replyText);
    console.log("Type of replyText:", typeof replyText);

    const composeBoxSelectors = [
        '[role="textbox"][g_editable="true"]',
        '.Ar.Au',
        '.Am.Al.editable.LW-avf'
    ];

    let composeBox = null;
    for (const selector of composeBoxSelectors) {
        composeBox = document.querySelector(selector);
        if (composeBox) {
            console.log("Found compose box with selector:", selector);
            break;
        }
    }

    if (composeBox) {
        // Clean the text but keep actual newlines (don't convert to <br>)
        let cleanReply = replyText
            .replace(/\\n/g, '\n')
            .trim();

        console.log("Cleaned reply:", cleanReply);

        composeBox.focus();

        // Use insertText with plain text (no HTML)
        document.execCommand('insertText', false, cleanReply);

        console.log("Reply inserted successfully!");
        return true;
    }

    console.error("Compose box not found");
    return false;
}


// Initialize the extension
function initExtension() {
    // Try to inject button immediately in case compose is already open
    injectButton();

    // Set up mutation observer to detect when compose window opens
    const observer = new MutationObserver((mutations) => {
        for (const mutation of mutations) {
            const addedNodes = Array.from(mutation.addedNodes);
            const hasComposeElements = addedNodes.some(node =>
                    node.nodeType === Node.ELEMENT_NODE && (
                        node.matches('.aDh, .btC, [role="dialog"]') ||
                        node.querySelector('.aDh, .btC, [role="dialog"]')
                    )
            );

            if (hasComposeElements) {
                injectButton();
            }
        }
    });

    observer.observe(document.body, {
        childList: true,
        subtree: true
    });

    console.log("Email Reply Extension initialized");
}

// Start the extension when DOM is fully loaded
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initExtension);
} else {
    initExtension();
}