# ==============================================================================
# Script: generate_presentation.ps1
# Description: Generates a high-quality, professional 10-slide PowerPoint presentation
#              for QuizSphere Mobile using Microsoft PowerPoint COM automation.
# Presenters: Giri Siddharth (40013), Ankit Singh (40003), Raju Ranjan Choudhary (40043)
# ==============================================================================

$ErrorActionPreference = "Stop"

# Helper to convert RGB to integer for COM
function RGB([int]$r, [int]$g, [int]$b) {
    return [int]($r + ($g * 256) + ($b * 65536))
}

# Color definitions (Modern Dark Theme with Royal Blue, Cyan, Emerald & Slate)
$C_BG        = RGB 15 23 42      # #0F172A (Deep Slate Navy)
$C_CARD_BG   = RGB 30 41 59      # #1E293B (Navy Card)
$C_CARD_ALT  = RGB 24 33 50      # #182132 (Inner Card)
$C_BORDER    = RGB 51 65 85      # #334155 (Card Border)
$C_BORDER_ACC= RGB 59 130 246    # #3B82F6 (Blue Accent Border)
$C_TITLE     = RGB 248 250 252   # #F8FAFC (White/Off-white)
$C_SUBTITLE  = RGB 148 163 184   # #94A3B8 (Cool Slate)
$C_ACCENT    = RGB 56 189 248    # #38BDF8 (Sky/Cyan)
$C_GREEN     = RGB 16 185 129    # #10B981 (Emerald)
$C_GREEN_BG  = RGB 6 78 59       # #064E3B (Dark Emerald)
$C_RED       = RGB 239 68 68     # #EF4444 (Crimson)
$C_RED_BG    = RGB 69 10 10      # #450A0A (Dark Red)
$C_YELLOW    = RGB 245 158 11    # #F59E0B (Amber)
$C_TEXT_BODY = RGB 226 232 240   # #E2E8F0 (Light Slate Body)
$C_TEXT_MUTED= RGB 148 163 184   # #94A3B8 (Muted Body)

$FONT_HEADING = "Segoe UI"
$FONT_BODY    = "Segoe UI"

# PowerPoint COM Constants
$msoShapeRectangle = 1
$msoShapeRoundedRectangle = 5
$msoFalse = 0
$msoTrue  = -1
$ppLayoutBlank = 12

Write-Host "Initializing PowerPoint Application..."
$app = New-Object -ComObject PowerPoint.Application
$pres = $app.Presentations.Add($msoFalse)

# Configure 16:9 Widescreen (960 x 540 points)
$pres.PageSetup.SlideWidth = 960
$pres.PageSetup.SlideHeight = 540

# Helper function to create slide base (Background, Header Badge, Title, Subtitle, Footer)
function Add-StandardSlideHeader($slideNumber, $badgeText, $titleText, $subtitleText) {
    $slide = $pres.Slides.Add($slideNumber, $ppLayoutBlank)

    # 1. Slide Background
    $bg = $slide.Shapes.AddShape($msoShapeRectangle, 0, 0, 960, 540)
    $bg.Fill.Solid()
    $bg.Fill.ForeColor.RGB = $C_BG
    $bg.Line.Visible = $msoFalse

    # 2. Header Badge Pill
    $badge = $slide.Shapes.AddShape($msoShapeRoundedRectangle, 45, 22, 280, 24)
    $badge.Fill.Solid()
    $badge.Fill.ForeColor.RGB = $C_CARD_BG
    $badge.Line.Visible = $msoTrue
    $badge.Line.ForeColor.RGB = $C_ACCENT
    $badge.Line.Weight = 1.0
    $badge.TextFrame.MarginLeft = 8
    $badge.TextFrame.MarginTop = 2
    $badge.TextFrame.WordWrap = $msoFalse
    $badge.TextFrame.TextRange.Text = $badgeText.ToUpper()
    $badge.TextFrame.TextRange.Font.Name = $FONT_HEADING
    $badge.TextFrame.TextRange.Font.Size = 9.5
    $badge.TextFrame.TextRange.Font.Bold = $msoTrue
    $badge.TextFrame.TextRange.Font.Color.RGB = $C_ACCENT

    # 3. Slide Title
    $titleBox = $slide.Shapes.AddTextbox(1, 42, 48, 870, 42)
    $titleBox.TextFrame.MarginLeft = 0
    $titleBox.TextFrame.MarginTop = 0
    $titleBox.TextFrame.WordWrap = $msoTrue
    $titleBox.TextFrame.TextRange.Text = $titleText
    $titleBox.TextFrame.TextRange.Font.Name = $FONT_HEADING
    $titleBox.TextFrame.TextRange.Font.Size = 24
    $titleBox.TextFrame.TextRange.Font.Bold = $msoTrue
    $titleBox.TextFrame.TextRange.Font.Color.RGB = $C_TITLE

    # 4. Slide Subtitle
    $subBox = $slide.Shapes.AddTextbox(1, 43, 90, 870, 24)
    $subBox.TextFrame.MarginLeft = 0
    $subBox.TextFrame.MarginTop = 0
    $subBox.TextFrame.WordWrap = $msoTrue
    $subBox.TextFrame.TextRange.Text = $subtitleText
    $subBox.TextFrame.TextRange.Font.Name = $FONT_BODY
    $subBox.TextFrame.TextRange.Font.Size = 12
    $subBox.TextFrame.TextRange.Font.Color.RGB = $C_SUBTITLE

    # 5. Slide Footer Bar
    $footerLine = $slide.Shapes.AddShape($msoShapeRectangle, 45, 508, 870, 1)
    $footerLine.Fill.Solid()
    $footerLine.Fill.ForeColor.RGB = $C_BORDER
    $footerLine.Line.Visible = $msoFalse

    $footerText = $slide.Shapes.AddTextbox(1, 45, 513, 870, 20)
    $footerText.TextFrame.MarginLeft = 0
    $footerText.TextFrame.MarginTop = 0
    $footerText.TextFrame.TextRange.Text = "QuizSphere Mobile | Android & Django Assessment Platform | Giri Siddharth, Ankit Singh, Raju Ranjan | Slide $slideNumber of 10"
    $footerText.TextFrame.TextRange.Font.Name = $FONT_BODY
    $footerText.TextFrame.TextRange.Font.Size = 9
    $footerText.TextFrame.TextRange.Font.Color.RGB = $C_TEXT_MUTED

    return $slide
}

# ==============================================================================
# SLIDE 1: TITLE SLIDE
# ==============================================================================
Write-Host "Generating Slide 1: Title Slide..."
$s1 = $pres.Slides.Add(1, $ppLayoutBlank)
$bg1 = $s1.Shapes.AddShape($msoShapeRectangle, 0, 0, 960, 540)
$bg1.Fill.Solid()
$bg1.Fill.ForeColor.RGB = $C_BG
$bg1.Line.Visible = $msoFalse

# Top Badge Pill
$pill1 = $s1.Shapes.AddShape($msoShapeRoundedRectangle, 305, 45, 350, 28)
$pill1.Fill.Solid()
$pill1.Fill.ForeColor.RGB = $C_CARD_BG
$pill1.Line.Visible = $msoTrue
$pill1.Line.ForeColor.RGB = $C_BORDER_ACC
$pill1.Line.Weight = 1.2
$pill1.TextFrame.TextRange.Text = "ACADEMIC MINI PROJECT PRESENTATION - 2026"
$pill1.TextFrame.TextRange.Font.Name = $FONT_HEADING
$pill1.TextFrame.TextRange.Font.Size = 10.5
$pill1.TextFrame.TextRange.Font.Bold = $msoTrue
$pill1.TextFrame.TextRange.Font.Color.RGB = $C_ACCENT
$pill1.TextFrame.TextRange.ParagraphFormat.Alignment = 2 # Center

# App Main Title
$t1 = $s1.Shapes.AddTextbox(1, 80, 85, 800, 75)
$t1.TextFrame.WordWrap = $msoTrue
$t1.TextFrame.TextRange.Text = "QuizSphere Mobile"
$t1.TextFrame.TextRange.Font.Name = $FONT_HEADING
$t1.TextFrame.TextRange.Font.Size = 48
$t1.TextFrame.TextRange.Font.Bold = $msoTrue
$t1.TextFrame.TextRange.Font.Color.RGB = $C_TITLE
$t1.TextFrame.TextRange.ParagraphFormat.Alignment = 2

# Subtitle
$sub1 = $s1.Shapes.AddTextbox(1, 80, 160, 800, 48)
$sub1.TextFrame.WordWrap = $msoTrue
$sub1.TextFrame.TextRange.Text = "Next-Generation Interactive Mobile Assessment Platform`r`nNative Android Client with Offline-First SQLite Engine & Real-Time Evaluation"
$sub1.TextFrame.TextRange.Font.Name = $FONT_BODY
$sub1.TextFrame.TextRange.Font.Size = 14
$sub1.TextFrame.TextRange.Font.Color.RGB = $C_SUBTITLE
$sub1.TextFrame.TextRange.ParagraphFormat.Alignment = 2

# Accent Divider Line
$div1 = $s1.Shapes.AddShape($msoShapeRectangle, 400, 222, 160, 3)
$div1.Fill.Solid()
$div1.Fill.ForeColor.RGB = $C_BORDER_ACC
$div1.Line.Visible = $msoFalse

# Section Header for Presenters
$lblPres = $s1.Shapes.AddTextbox(1, 80, 235, 800, 25)
$lblPres.TextFrame.TextRange.Text = "PRESENTED BY"
$lblPres.TextFrame.TextRange.Font.Name = $FONT_HEADING
$lblPres.TextFrame.TextRange.Font.Size = 11
$lblPres.TextFrame.TextRange.Font.Bold = $msoTrue
$lblPres.TextFrame.TextRange.Font.Color.RGB = $C_ACCENT
$lblPres.TextFrame.TextRange.ParagraphFormat.Alignment = 2

# 3 Presenter Cards
$team = @(
    @{ Name = "Giri Siddharth"; Roll = "40013"; Focus = "Mobile Architecture & SQLite Engine" },
    @{ Name = "Ankit Singh"; Roll = "40003"; Focus = "UI/UX & Interactive Quiz Lifecycle" },
    @{ Name = "Raju Ranjan Choudhary"; Roll = "40043"; Focus = "Analytics, Scoring & Security Layer" }
)

$cardW = 265
$cardH = 155
$cardY = 268
$startX = 55
$spacing = 38

for ($i = 0; $i -lt 3; $i++) {
    $x = $startX + ($i * ($cardW + $spacing))
    $pCard = $s1.Shapes.AddShape($msoShapeRoundedRectangle, $x, $cardY, $cardW, $cardH)
    $pCard.Fill.Solid()
    $pCard.Fill.ForeColor.RGB = $C_CARD_BG
    $pCard.Line.Visible = $msoTrue
    $pCard.Line.ForeColor.RGB = $C_BORDER
    $pCard.Line.Weight = 1.2

    # Text inside card
    $tf = $pCard.TextFrame
    $tf.MarginLeft = 14
    $tf.MarginTop = 16
    $tf.WordWrap = $msoTrue

    $tr = $tf.TextRange
    $tr.Text = "$($team[$i].Name)`r`nRoll No: $($team[$i].Roll)`r`n`r`nCore Focus:`r`n$($team[$i].Focus)"
    $tr.Font.Name = $FONT_BODY
    $tr.Font.Size = 12
    $tr.Font.Color.RGB = $C_TEXT_BODY
    $tr.ParagraphFormat.Alignment = 2

    # Bold the name
    $tr.Paragraphs(1).Font.Size = 15
    $tr.Paragraphs(1).Font.Bold = $msoTrue
    $tr.Paragraphs(1).Font.Color.RGB = $C_TITLE
    $tr.Paragraphs(2).Font.Size = 12
    $tr.Paragraphs(2).Font.Bold = $msoTrue
    $tr.Paragraphs(2).Font.Color.RGB = $C_ACCENT
}

# Slide 1 Footer
$f1 = $s1.Shapes.AddTextbox(1, 80, 485, 800, 25)
$f1.TextFrame.TextRange.Text = "Technology: Java 17 | Android SDK | SQLite | Material Design | Django REST Ecosystem"
$f1.TextFrame.TextRange.Font.Name = $FONT_BODY
$f1.TextFrame.TextRange.Font.Size = 10
$f1.TextFrame.TextRange.Font.Color.RGB = $C_TEXT_MUTED
$f1.TextFrame.TextRange.ParagraphFormat.Alignment = 2

# ==============================================================================
# SLIDE 2: INTRODUCTION OF PROJECT & PROBLEM STATEMENT
# ==============================================================================
Write-Host "Generating Slide 2: Introduction & Problem Statement..."
$s2 = Add-StandardSlideHeader 2 "01. PROJECT OVERVIEW" "Introduction & Problem Statement" "Addressing structural inefficiencies of conventional paper-based testing with modern mobile software"

# Two large side-by-side comparison cards
$cW = 415
$cH = 355
$cY = 130

# Left Card: Traditional Pen & Paper Testing
$cardTrad = $s2.Shapes.AddShape($msoShapeRoundedRectangle, 45, $cY, $cW, $cH)
$cardTrad.Fill.Solid()
$cardTrad.Fill.ForeColor.RGB = $C_CARD_BG
$cardTrad.Line.Visible = $msoTrue
$cardTrad.Line.ForeColor.RGB = $C_RED
$cardTrad.Line.Weight = 1.5

$tfTrad = $cardTrad.TextFrame
$tfTrad.MarginLeft = 20
$tfTrad.MarginTop = 18
$tfTrad.MarginRight = 20
$tfTrad.WordWrap = $msoTrue
$tfTrad.TextRange.Text = "TRADITIONAL TESTING LIMITATIONS`r`n`r`n" +
    "[x] High Evaluation Latency: Days or weeks elapse before students receive grades and remedial feedback.`r`n`r`n" +
    "[x] Labor-Intensive & Error-Prone: Manual paper distribution, tallying scores, and recording results invite calculation errors.`r`n`r`n" +
    "[x] Zero Analytical Insights: Instructors receive raw totals without question-level failure rates or time-per-question metrics.`r`n`r`n" +
    "[x] Lack of Accessibility: Fixed physical locations and scheduled paper sessions limit learning flexibility.`r`n`r`n" +
    "[x] Resource Waste & Costs: Recurring expenditure on question paper printing, stationery, and physical storage."

$tfTrad.TextRange.Font.Name = $FONT_BODY
$tfTrad.TextRange.Font.Size = 11.5
$tfTrad.TextRange.Font.Color.RGB = $C_TEXT_BODY
$tfTrad.TextRange.Paragraphs(1).Font.Size = 14
$tfTrad.TextRange.Paragraphs(1).Font.Bold = $msoTrue
$tfTrad.TextRange.Paragraphs(1).Font.Color.RGB = $C_RED

# Right Card: The QuizSphere Mobile Solution
$cardSol = $s2.Shapes.AddShape($msoShapeRoundedRectangle, 500, $cY, $cW, $cH)
$cardSol.Fill.Solid()
$cardSol.Fill.ForeColor.RGB = $C_CARD_BG
$cardSol.Line.Visible = $msoTrue
$cardSol.Line.ForeColor.RGB = $C_GREEN
$cardSol.Line.Weight = 1.5

$tfSol = $cardSol.TextFrame
$tfSol.MarginLeft = 20
$tfSol.MarginTop = 18
$tfSol.MarginRight = 20
$tfSol.WordWrap = $msoTrue
$tfSol.TextRange.Text = "THE QUIZSPHERE MOBILE SOLUTION`r`n`r`n" +
    "[+] Instant Auto-Grading: Submissions scored in milliseconds with precise pass/fail percentages.`r`n`r`n" +
    "[+] Offline-First SQLite Architecture: Take full multi-domain assessments without requiring an active internet connection.`r`n`r`n" +
    "[+] Enforced Exam Integrity: Automated countdown timer with instant background submission on time expiry.`r`n`r`n" +
    "[+] Granular Diagnostic Review: Question-by-question breakdown with chosen options, correct answers, and rich explanations.`r`n`r`n" +
    "[+] Historical Performance Tracking: Dedicated attempt logs, rank leaderboards, and personalized analytics dashboard."

$tfSol.TextRange.Font.Name = $FONT_BODY
$tfSol.TextRange.Font.Size = 11.5
$tfSol.TextRange.Font.Color.RGB = $C_TEXT_BODY
$tfSol.TextRange.Paragraphs(1).Font.Size = 14
$tfSol.TextRange.Paragraphs(1).Font.Bold = $msoTrue
$tfSol.TextRange.Paragraphs(1).Font.Color.RGB = $C_GREEN

# ==============================================================================
# SLIDE 3: PROJECT OBJECTIVES
# ==============================================================================
Write-Host "Generating Slide 3: Project Objectives..."
$s3 = Add-StandardSlideHeader 3 "02. CORE GOALS" "Project Objectives & Technical Milestones" "Six foundational objectives engineered to deliver a production-grade assessment ecosystem"

# 6 Cards Grid (2 rows x 3 columns)
$objCols = 3
$oW = 274
$oH = 168
$oStartX = 45
$oStartY = 132
$oGapX = 24
$oGapY = 18

$objectives = @(
    @{
        Num = "01"; Title = "Automated Real-Time Grading";
        Desc = "Eliminate grading latency by executing instant score calculation, percentage evaluation, and pass/fail determination the moment an attempt is submitted."
    },
    @{
        Num = "02"; Title = "Role-Based User Portals";
        Desc = "Provide secure, isolated workflows: dedicated candidate interface for testing and attempt audits, alongside administrator controls for content management."
    },
    @{
        Num = "03"; Title = "Dynamic Multi-Domain Engine";
        Desc = "Support dynamic topic-based categories (Django, Python, Web Tech, Database SQL) and extensible question banks without modifying source code or schemas."
    },
    @{
        Num = "04"; Title = "Strict Timed Exam Integrity";
        Desc = "Enforce countdown time boundaries via background-supervised CountDownTimer, triggering automated submission when time expires to ensure fairness."
    },
    @{
        Num = "05"; Title = "Diagnostic Review & Analytics";
        Desc = "Provide comprehensive post-exam reviews showing selected options, correct answers, detailed rationale, and dynamic leaderboard rankings."
    },
    @{
        Num = "06"; Title = "Native Mobile Optimization";
        Desc = "Engineer an offline-first Android application leveraging native SQLite database caching, smooth Material Design components, and sub-100ms UI response."
    }
)

for ($idx = 0; $idx -lt $objectives.Count; $idx++) {
    $r = [Math]::Floor($idx / $objCols)
    $c = $idx % $objCols
    $ox = $oStartX + ($c * ($oW + $oGapX))
    $oy = $oStartY + ($r * ($oH + $oGapY))

    $oCard = $s3.Shapes.AddShape($msoShapeRoundedRectangle, $ox, $oy, $oW, $oH)
    $oCard.Fill.Solid()
    $oCard.Fill.ForeColor.RGB = $C_CARD_BG
    $oCard.Line.Visible = $msoTrue
    $oCard.Line.ForeColor.RGB = $C_BORDER
    $oCard.Line.Weight = 1.0

    $tfO = $oCard.TextFrame
    $tfO.MarginLeft = 14
    $tfO.MarginTop = 12
    $tfO.MarginRight = 14
    $tfO.WordWrap = $msoTrue

    $item = $objectives[$idx]
    $tfO.TextRange.Text = "$($item.Num). $($item.Title)`r`n`r`n$($item.Desc)"
    $tfO.TextRange.Font.Name = $FONT_BODY
    $tfO.TextRange.Font.Size = 10.5
    $tfO.TextRange.Font.Color.RGB = $C_TEXT_BODY

    # Heading formatting
    $tfO.TextRange.Paragraphs(1).Font.Name = $FONT_HEADING
    $tfO.TextRange.Paragraphs(1).Font.Size = 12
    $tfO.TextRange.Paragraphs(1).Font.Bold = $msoTrue
    $tfO.TextRange.Paragraphs(1).Font.Color.RGB = $C_ACCENT
}

# ==============================================================================
# SLIDE 4: TECHNOLOGY STACK
# ==============================================================================
Write-Host "Generating Slide 4: Technology Stack..."
$s4 = Add-StandardSlideHeader 4 "03. SYSTEM TECH STACK" "Comprehensive Technology Stack" "End-to-end technologies powering Android client, relational persistence, and backend architecture"

# 4 Columns for Tech Stack
$tW = 202
$tH = 355
$tStartX = 45
$tGap = 20
$tY = 130

$techLayers = @(
    @{
        Header = "MOBILE FRONTEND";
        Color = $C_BORDER_ACC;
        Points = @(
            "Java 17 (Native Android)",
            "Android SDK (API 24 - 34)",
            "Google Material Design 3",
            "Custom XML Layouts",
            "RecyclerView & ViewHolders",
            "Vector Drawables & Badges",
            "CountDownTimer Lifecycle"
        )
    },
    @{
        Header = "DATABASE & STORAGE";
        Color = $C_GREEN;
        Points = @(
            "SQLite Embedded Engine",
            "SQLiteOpenHelper Architecture",
            "Parameterized Queries (DAO)",
            "Multi-table Relational Joins",
            "ContentValues & Cursors",
            "SharedPreferences Session",
            "ACID Compliant DB"
        )
    },
    @{
        Header = "BACKEND & INTEGRATION";
        Color = $C_ACCENT;
        Points = @(
            "Python 3.11+ Runtime",
            "Django 5.x Web Framework",
            "Django REST Framework (DRF)",
            "JSON Data Serialization",
            "Django ORM & Models",
            "Session-based Authentication",
            "Gunicorn / Nginx Deployment"
        )
    },
    @{
        Header = "BUILD & TOOLING";
        Color = $C_YELLOW;
        Points = @(
            "Gradle 9.6 Build Automation",
            "Android Studio Hedgehog/Giraffe",
            "Git & GitHub Version Control",
            "ADB & Logcat Diagnostics",
            "ProGuard / R8 Shrinking",
            "JUnit & AndroidX Test Suite",
            "APK Package Packaging"
        )
    }
)

for ($i = 0; $i -lt 4; $i++) {
    $tx = $tStartX + ($i * ($tW + $tGap))
    $layer = $techLayers[$i]

    $tCard = $s4.Shapes.AddShape($msoShapeRoundedRectangle, $tx, $tY, $tW, $tH)
    $tCard.Fill.Solid()
    $tCard.Fill.ForeColor.RGB = $C_CARD_BG
    $tCard.Line.Visible = $msoTrue
    $tCard.Line.ForeColor.RGB = $layer.Color
    $tCard.Line.Weight = 1.2

    $tfT = $tCard.TextFrame
    $tfT.MarginLeft = 14
    $tfT.MarginTop = 16
    $tfT.MarginRight = 14
    $tfT.WordWrap = $msoTrue

    $text = "$($layer.Header)`r`n`r`n"
    foreach ($p in $layer.Points) {
        $text += "- $p`r`n`r`n"
    }

    $tfT.TextRange.Text = $text.TrimEnd()
    $tfT.TextRange.Font.Name = $FONT_BODY
    $tfT.TextRange.Font.Size = 10.5
    $tfT.TextRange.Font.Color.RGB = $C_TEXT_BODY

    # Header style
    $tfT.TextRange.Paragraphs(1).Font.Name = $FONT_HEADING
    $tfT.TextRange.Paragraphs(1).Font.Size = 12.5
    $tfT.TextRange.Paragraphs(1).Font.Bold = $msoTrue
    $tfT.TextRange.Paragraphs(1).Font.Color.RGB = $layer.Color
}

# ==============================================================================
# SLIDE 5: SYSTEM ARCHITECTURE
# ==============================================================================
Write-Host "Generating Slide 5: System Architecture..."
$s5 = Add-StandardSlideHeader 5 "04. SYSTEM ARCHITECTURE" "Tiered System Architecture & Data Flow" "Loosely-coupled multi-tier architecture supporting offline execution and cloud synchronization"

# 4 Horizontal Architecture Layers
$archLayers = @(
    @{
        Tier = "PRESENTATION TIER (UI & INTERACTION)";
        Color = $C_BORDER_ACC;
        Sub = "Android Activities & Adapters";
        Desc = "SplashActivity | LoginActivity | RegisterActivity | DashboardActivity | CategoryListActivity | QuizDetailActivity | QuizTakeActivity | QuizResultActivity | LeaderboardActivity | PresentationShowcaseActivity"
    },
    @{
        Tier = "CONTROLLER & BUSINESS LOGIC TIER";
        Color = $C_ACCENT;
        Sub = "Lifecycle Controllers & State Engines";
        Desc = "SessionManager (Auth Token & Auto-login) | CountDownTimer Supervisor | Live Option Selection State | Quiz Auto-Grading Engine | Score & Percentage Aggregator | Category Filtering Controller"
    },
    @{
        Tier = "DATA ACCESS TIER (DAO & REPOSITORY)";
        Color = $C_GREEN;
        Sub = "Relational Abstraction Engine";
        Desc = "QuizSphereDbHelper Singleton | Parameterized Query Builders | Cursor-to-Model Mappers (User, Category, Question, Option, QuizAttempt, AttemptAnswer) | Database Schema Lifecycle & Migrations"
    },
    @{
        Tier = "PERSISTENCE & CLOUD INTEGRATION TIER";
        Color = $C_YELLOW;
        Sub = "Local SQLite & Cloud REST Backend";
        Desc = "Local SQLite Embedded Engine (quizsphere.db) | SharedPreferences Key-Value Store | Remote Django REST Framework Endpoints (REST API) | Remote PostgreSQL / Cloud Database for cross-device sync"
    }
)

$aW = 870
$aH = 75
$aStartY = 130
$aGapY = 16

for ($i = 0; $i -lt 4; $i++) {
    $ay = $aStartY + ($i * ($aH + $aGapY))
    $layer = $archLayers[$i]

    $aCard = $s5.Shapes.AddShape($msoShapeRoundedRectangle, 45, $ay, $aW, $aH)
    $aCard.Fill.Solid()
    $aCard.Fill.ForeColor.RGB = $C_CARD_BG
    $aCard.Line.Visible = $msoTrue
    $aCard.Line.ForeColor.RGB = $layer.Color
    $aCard.Line.Weight = 1.2

    $tfA = $aCard.TextFrame
    $tfA.MarginLeft = 18
    $tfA.MarginTop = 10
    $tfA.MarginRight = 18
    $tfA.WordWrap = $msoTrue

    $tfA.TextRange.Text = "$($layer.Tier) -- $($layer.Sub)`r`n$($layer.Desc)"
    $tfA.TextRange.Font.Name = $FONT_BODY
    $tfA.TextRange.Font.Size = 10.5
    $tfA.TextRange.Font.Color.RGB = $C_TEXT_BODY

    # Header style
    $tfA.TextRange.Paragraphs(1).Font.Name = $FONT_HEADING
    $tfA.TextRange.Paragraphs(1).Font.Size = 12
    $tfA.TextRange.Paragraphs(1).Font.Bold = $msoTrue
    $tfA.TextRange.Paragraphs(1).Font.Color.RGB = $layer.Color
}

# Add Architecture arrows or connector text below
$noteBox = $s5.Shapes.AddTextbox(1, 45, 480, 870, 22)
$noteBox.TextFrame.MarginLeft = 0
$noteBox.TextFrame.MarginTop = 0
$noteBox.TextFrame.TextRange.Text = "Data Flow: User Action -> Activity View -> Session/Timer Controller -> SQLite Helper (DAO) -> Database Persistence -> Instant UI Refresh"
$noteBox.TextFrame.TextRange.Font.Name = $FONT_HEADING
$noteBox.TextFrame.TextRange.Font.Size = 10
$noteBox.TextFrame.TextRange.Font.Bold = $msoTrue
$noteBox.TextFrame.TextRange.Font.Color.RGB = $C_ACCENT
$noteBox.TextFrame.TextRange.ParagraphFormat.Alignment = 2

# ==============================================================================
# SLIDE 6: KEY FEATURES
# ==============================================================================
Write-Host "Generating Slide 6: Key Features..."
$s6 = Add-StandardSlideHeader 6 "05. PLATFORM CAPABILITIES" "Key Features & Functional Highlights" "Interactive feature set built to enhance candidate experience and ensure robust assessment control"

$features = @(
    @{
        Title = "User Registration & Session Auth";
        Points = "- Smooth signup and login flows`r`n- Persistent auto-login via SessionManager`r`n- Role separation for students and administrators"
    },
    @{
        Title = "Category-Driven Assessment";
        Points = "- Modular topic catalog (Django, Python, Web, SQL)`r`n- Configurable time limits (mins) & pass % thresholds`r`n- Dynamic question count and rules preview"
    },
    @{
        Title = "Precision Timed Quiz Engine";
        Points = "- Background CountDownTimer with tick alerts`r`n- Smooth question-by-question navigation`r`n- Automated instant submit on countdown expiry"
    },
    @{
        Title = "Instant Millisecond Auto-Grading";
        Points = "- Zero-latency score and percentage evaluation`r`n- Dynamic pass/fail status calculation`r`n- Total marks and completion time tracking"
    },
    @{
        Title = "Granular Post-Quiz Review";
        Points = "- Question-by-question outcome breakdown`r`n- Highlights user selected answer vs correct answer`r`n- Rich conceptual explanations for remediation"
    },
    @{
        Title = "Ranked Leaderboard & Analytics";
        Points = "- Category-wise and global candidate rankings`r`n- High-score aggregation with attempt timestamps`r`n- Personal dashboard with historical attempt logs"
    }
)

for ($idx = 0; $idx -lt $features.Count; $idx++) {
    $r = [Math]::Floor($idx / 3)
    $c = $idx % 3
    $fx = 45 + ($c * (274 + 24))
    $fy = 132 + ($r * (168 + 18))

    $fCard = $s6.Shapes.AddShape($msoShapeRoundedRectangle, $fx, $fy, 274, 168)
    $fCard.Fill.Solid()
    $fCard.Fill.ForeColor.RGB = $C_CARD_BG
    $fCard.Line.Visible = $msoTrue
    $fCard.Line.ForeColor.RGB = $C_BORDER
    $fCard.Line.Weight = 1.0

    $tfF = $fCard.TextFrame
    $tfF.MarginLeft = 14
    $tfF.MarginTop = 14
    $tfF.MarginRight = 14
    $tfF.WordWrap = $msoTrue

    $item = $features[$idx]
    $tfF.TextRange.Text = "$($idx + 1). $($item.Title)`r`n`r`n$($item.Points)"
    $tfF.TextRange.Font.Name = $FONT_BODY
    $tfF.TextRange.Font.Size = 10.5
    $tfF.TextRange.Font.Color.RGB = $C_TEXT_BODY

    $tfF.TextRange.Paragraphs(1).Font.Name = $FONT_HEADING
    $tfF.TextRange.Paragraphs(1).Font.Size = 12
    $tfF.TextRange.Paragraphs(1).Font.Bold = $msoTrue
    $tfF.TextRange.Paragraphs(1).Font.Color.RGB = $C_ACCENT
}

# ==============================================================================
# SLIDE 7: DATABASE DESIGN
# ==============================================================================
Write-Host "Generating Slide 7: Database Design..."
$s7 = Add-StandardSlideHeader 7 "06. DATA ARCHITECTURE" "Database Design & Relational Schema" "Production SQLite schema with 6 relational tables, strict constraints, and detailed audit trails"

$tables = @(
    @{
        Name = "users";
        Role = "Candidate & Admin Credentials";
        Cols = "- id: INTEGER (PRIMARY KEY AUTOINCREMENT)`r`n- username: TEXT (UNIQUE NOT NULL)`r`n- email: TEXT (OPTIONAL)`r`n- password: TEXT (ENCRYPTED / HASHED)`r`n- role: TEXT ('student' / 'admin')"
    },
    @{
        Name = "categories";
        Role = "Assessment Subject Tracks";
        Cols = "- id: INTEGER (PRIMARY KEY AUTOINCREMENT)`r`n- name: TEXT NOT NULL (e.g. 'Django Framework')`r`n- slug: TEXT UNIQUE ('django-framework')`r`n- description: TEXT (Track syllabus summary)`r`n- time_limit_mins: INTEGER | pass_percentage: INT"
    },
    @{
        Name = "questions & options";
        Role = "Question Bank & Answer Options";
        Cols = "- questions: id (PK), category_id (FK), text, marks, explanation`r`n- options: id (PK), question_id (FK), text, is_correct (BOOLEAN 1/0)`r`n- Foreign Key CASCADE enforces data integrity`r`n- 1 Category -> N Questions | 1 Question -> 4 Options"
    },
    @{
        Name = "quiz_attempts & attempt_answers";
        Role = "Exam History & Audit Trail";
        Cols = "- quiz_attempts: id (PK), user_id (FK), category_id (FK), score, total_marks, percentage, passed, time_taken, date_taken`r`n- attempt_answers: id (PK), attempt_id (FK), question_id (FK), selected_option_text, correct_option_text, is_correct, explanation"
    }
)

$dbW = 415
$dbH = 155
$dbStartY = 132

for ($i = 0; $i -lt 4; $i++) {
    $r = [Math]::Floor($i / 2)
    $c = $i % 2
    $dx = 45 + ($c * ($dbW + 40))
    $dy = $dbStartY + ($r * ($dbH + 16))

    $dCard = $s7.Shapes.AddShape($msoShapeRoundedRectangle, $dx, $dy, $dbW, $dbH)
    $dCard.Fill.Solid()
    $dCard.Fill.ForeColor.RGB = $C_CARD_BG
    $dCard.Line.Visible = $msoTrue
    $dCard.Line.ForeColor.RGB = $C_BORDER_ACC
    $dCard.Line.Weight = 1.0

    $tfD = $dCard.TextFrame
    $tfD.MarginLeft = 14
    $tfD.MarginTop = 10
    $tfD.MarginRight = 14
    $tfD.WordWrap = $msoTrue

    $item = $tables[$i]
    $tfD.TextRange.Text = "TABLE: $($item.Name.ToUpper()) ($($item.Role))`r`n$($item.Cols)"
    $tfD.TextRange.Font.Name = $FONT_BODY
    $tfD.TextRange.Font.Size = 10
    $tfD.TextRange.Font.Color.RGB = $C_TEXT_BODY

    $tfD.TextRange.Paragraphs(1).Font.Name = $FONT_HEADING
    $tfD.TextRange.Paragraphs(1).Font.Size = 11.5
    $tfD.TextRange.Paragraphs(1).Font.Bold = $msoTrue
    $tfD.TextRange.Paragraphs(1).Font.Color.RGB = $C_ACCENT
}

# Bottom Cardinality Banner
$cardBanner = $s7.Shapes.AddShape($msoShapeRoundedRectangle, 45, 470, 870, 30)
$cardBanner.Fill.Solid()
$cardBanner.Fill.ForeColor.RGB = $C_CARD_ALT
$cardBanner.Line.Visible = $msoTrue
$cardBanner.Line.ForeColor.RGB = $C_GREEN
$cardBanner.Line.Weight = 1.0
$cardBanner.TextFrame.TextRange.Text = "Relational Cardinality:  1 Category -> Many Questions  |  1 Question -> 4 Options  |  1 User -> Many Attempts  |  1 Attempt -> Many Answer Records"
$cardBanner.TextFrame.TextRange.Font.Name = $FONT_HEADING
$cardBanner.TextFrame.TextRange.Font.Size = 10.5
$cardBanner.TextFrame.TextRange.Font.Bold = $msoTrue
$cardBanner.TextFrame.TextRange.Font.Color.RGB = $C_GREEN
$cardBanner.TextFrame.TextRange.ParagraphFormat.Alignment = 2

# ==============================================================================
# SLIDE 8: END-TO-END WORKFLOW
# ==============================================================================
Write-Host "Generating Slide 8: End-to-End Workflow..."
$s8 = Add-StandardSlideHeader 8 "07. CANDIDATE LIFECYCLE" "End-to-End User Assessment Workflow" "Step-by-step candidate execution journey from authentication to real-time analytics"

$steps = @(
    @{
        Step = "STEP 01"; Title = "Auth & Session Setup";
        Desc = "Candidate signs up or logs in. SessionManager verifies credentials in users table and caches session token."
    },
    @{
        Step = "STEP 02"; Title = "Category Selection";
        Desc = "Candidate browses categories on DashboardActivity. Reviews track rules, questions count, and time limits."
    },
    @{
        Step = "STEP 03"; Title = "Timed Quiz Session";
        Desc = "QuizTakeActivity launches CountDownTimer. Candidate navigates dynamic questions and selects radio options."
    },
    @{
        Step = "STEP 04"; Title = "Auto-Grading Trigger";
        Desc = "On submit or timer expiry, engine compares answers against options table and inserts quiz_attempts in SQLite."
    },
    @{
        Step = "STEP 05"; Title = "Diagnostics & Rank";
        Desc = "QuizResultActivity shows score, %, pass/fail badge, question review, and updates user rank on Leaderboard."
    }
)

$stW = 158
$stH = 345
$stStartX = 45
$stGap = 20
$stY = 135

for ($i = 0; $i -lt 5; $i++) {
    $sx = $stStartX + ($i * ($stW + $stGap))
    $item = $steps[$i]

    $sCard = $s8.Shapes.AddShape($msoShapeRoundedRectangle, $sx, $stY, $stW, $stH)
    $sCard.Fill.Solid()
    $sCard.Fill.ForeColor.RGB = $C_CARD_BG
    $sCard.Line.Visible = $msoTrue
    $sCard.Line.ForeColor.RGB = $C_BORDER_ACC
    $sCard.Line.Weight = 1.2

    $tfS = $sCard.TextFrame
    $tfS.MarginLeft = 12
    $tfS.MarginTop = 18
    $tfS.MarginRight = 12
    $tfS.WordWrap = $msoTrue

    $tfS.TextRange.Text = "$($item.Step)`r`n`r`n$($item.Title)`r`n`r`n$($item.Desc)"
    $tfS.TextRange.Font.Name = $FONT_BODY
    $tfS.TextRange.Font.Size = 10.5
    $tfS.TextRange.Font.Color.RGB = $C_TEXT_BODY
    $tfS.TextRange.ParagraphFormat.Alignment = 2

    # Step Badge style
    $tfS.TextRange.Paragraphs(1).Font.Name = $FONT_HEADING
    $tfS.TextRange.Paragraphs(1).Font.Size = 13
    $tfS.TextRange.Paragraphs(1).Font.Bold = $msoTrue
    $tfS.TextRange.Paragraphs(1).Font.Color.RGB = $C_ACCENT

    # Title style
    $tfS.TextRange.Paragraphs(3).Font.Name = $FONT_HEADING
    $tfS.TextRange.Paragraphs(3).Font.Size = 12
    $tfS.TextRange.Paragraphs(3).Font.Bold = $msoTrue
    $tfS.TextRange.Paragraphs(3).Font.Color.RGB = $C_TITLE
}

# ==============================================================================
# SLIDE 9: APPLICATION MODULES & SECURITY CONSIDERATIONS
# ==============================================================================
Write-Host "Generating Slide 9: App Modules & Security Considerations..."
$s9 = Add-StandardSlideHeader 9 "08. MODULARITY & SECURITY" "Application Modules & Security Architecture" "Componentized functional boundaries coupled with defense-in-depth security controls"

# Two major panels: Left (Application Modules), Right (Security Considerations)
$pW = 415
$pH = 355
$pY = 130

# Left Panel: Application Modules
$cardMod = $s9.Shapes.AddShape($msoShapeRoundedRectangle, 45, $pY, $pW, $pH)
$cardMod.Fill.Solid()
$cardMod.Fill.ForeColor.RGB = $C_CARD_BG
$cardMod.Line.Visible = $msoTrue
$cardMod.Line.ForeColor.RGB = $C_BORDER_ACC
$cardMod.Line.Weight = 1.2

$tfMod = $cardMod.TextFrame
$tfMod.MarginLeft = 18
$tfMod.MarginTop = 16
$tfMod.MarginRight = 18
$tfMod.WordWrap = $msoTrue

$tfMod.TextRange.Text = "APPLICATION MODULES`r`n`r`n" +
    "1. Authentication & Session Module`r`n" +
    "   - LoginActivity, RegisterActivity, SessionManager`r`n" +
    "   - Handles credential checks, token persistence & auto-login.`r`n`r`n" +
    "2. Quiz Engine & Timer Module`r`n" +
    "   - QuizTakeActivity, CountDownTimer, Question models`r`n" +
    "   - Manages real-time countdown, state & auto-submission.`r`n`r`n" +
    "3. Diagnostics & Analytics Module`r`n" +
    "   - QuizResultActivity, QuestionReviewAdapter, HistoryAdapter`r`n" +
    "   - Renders scorecards, answer diagnostics & leaderboards.`r`n`r`n" +
    "4. Admin & Showcase Walkthrough Module`r`n" +
    "   - PresentationShowcaseActivity & dynamic CRUD methods`r`n" +
    "   - Built-in showcase of slides and content management."

$tfMod.TextRange.Font.Name = $FONT_BODY
$tfMod.TextRange.Font.Size = 10.5
$tfMod.TextRange.Font.Color.RGB = $C_TEXT_BODY
$tfMod.TextRange.Paragraphs(1).Font.Name = $FONT_HEADING
$tfMod.TextRange.Paragraphs(1).Font.Size = 13.5
$tfMod.TextRange.Paragraphs(1).Font.Bold = $msoTrue
$tfMod.TextRange.Paragraphs(1).Font.Color.RGB = $C_BORDER_ACC

# Right Panel: Security Considerations
$cardSec = $s9.Shapes.AddShape($msoShapeRoundedRectangle, 500, $pY, $pW, $pH)
$cardSec.Fill.Solid()
$cardSec.Fill.ForeColor.RGB = $C_CARD_BG
$cardSec.Line.Visible = $msoTrue
$cardSec.Line.ForeColor.RGB = $C_GREEN
$cardSec.Line.Weight = 1.2

$tfSec = $cardSec.TextFrame
$tfSec.MarginLeft = 18
$tfSec.MarginTop = 16
$tfSec.MarginRight = 18
$tfSec.WordWrap = $msoTrue

$tfSec.TextRange.Text = "SECURITY CONSIDERATIONS`r`n`r`n" +
    "1. SQL Injection Immunity`r`n" +
    "   - 100% of SQLite database queries use parameterized placeholders (selectionArgs & ContentValues), preventing query injection.`r`n`r`n" +
    "2. Android Sandbox Storage Isolation`r`n" +
    "   - SQLite DB (quizsphere.db) and SharedPreferences reside strictly in app-private sandbox storage (/data/data/com.quizsphere.app).`r`n`r`n" +
    "3. In-Memory Answer & Timer Security`r`n" +
    "   - Correct answers are never sent to question view state; validation occurs against DB upon final submission.`r`n`r`n" +
    "4. Client-Side Input Sanitization`r`n" +
    "   - Defensive input validation for usernames, emails, and passwords prevents buffer issues and null pointer exceptions."

$tfSec.TextRange.Font.Name = $FONT_BODY
$tfSec.TextRange.Font.Size = 10.5
$tfSec.TextRange.Font.Color.RGB = $C_TEXT_BODY
$tfSec.TextRange.Paragraphs(1).Font.Name = $FONT_HEADING
$tfSec.TextRange.Paragraphs(1).Font.Size = 13.5
$tfSec.TextRange.Paragraphs(1).Font.Bold = $msoTrue
$tfSec.TextRange.Paragraphs(1).Font.Color.RGB = $C_GREEN

# ==============================================================================
# SLIDE 10: FUTURE ENHANCEMENTS & CONCLUSION
# ==============================================================================
Write-Host "Generating Slide 10: Future Enhancements & Conclusion..."
$s10 = Add-StandardSlideHeader 10 "09. ROADMAP & CONCLUSION" "Future Roadmap & Project Conclusion" "Strategic product evolution and wrap-up of technical milestones achieved"

# Left Panel: Future Roadmap
$cardFut = $s10.Shapes.AddShape($msoShapeRoundedRectangle, 45, $pY, $pW, $pH)
$cardFut.Fill.Solid()
$cardFut.Fill.ForeColor.RGB = $C_CARD_BG
$cardFut.Line.Visible = $msoTrue
$cardFut.Line.ForeColor.RGB = $C_ACCENT
$cardFut.Line.Weight = 1.2

$tfFut = $cardFut.TextFrame
$tfFut.MarginLeft = 18
$tfFut.MarginTop = 16
$tfFut.MarginRight = 18
$tfFut.WordWrap = $msoTrue

$tfFut.TextRange.Text = "FUTURE ENHANCEMENTS ROADMAP`r`n`r`n" +
    "- Cloud REST API Synchronization:`r`n" +
    "  Bidirectional sync between local SQLite and remote Django REST Framework server for cross-device access.`r`n`r`n" +
    "- AI-Powered Adaptive Testing:`r`n" +
    "  Dynamic question difficulty adjustment matching candidate skill levels using machine learning models.`r`n`r`n" +
    "- Real-Time Multiplayer Quiz Battles:`r`n" +
    "  WebSocket-based synchronous quiz duels and classroom tournaments.`r`n`r`n" +
    "- PDF Certificate Generation:`r`n" +
    "  Automated generation and export of verifiable score certificates.`r`n`r`n" +
    "- Push Notifications & Gamification:`r`n" +
    "  Daily practice streaks, achievement badges, and reminder notifications."

$tfFut.TextRange.Font.Name = $FONT_BODY
$tfFut.TextRange.Font.Size = 10.5
$tfFut.TextRange.Font.Color.RGB = $C_TEXT_BODY
$tfFut.TextRange.Paragraphs(1).Font.Name = $FONT_HEADING
$tfFut.TextRange.Paragraphs(1).Font.Size = 13.5
$tfFut.TextRange.Paragraphs(1).Font.Bold = $msoTrue
$tfFut.TextRange.Paragraphs(1).Font.Color.RGB = $C_ACCENT

# Right Panel: Project Conclusion & Team Sign-off
$cardCon = $s10.Shapes.AddShape($msoShapeRoundedRectangle, 500, $pY, $pW, $pH)
$cardCon.Fill.Solid()
$cardCon.Fill.ForeColor.RGB = $C_CARD_BG
$cardCon.Line.Visible = $msoTrue
$cardCon.Line.ForeColor.RGB = $C_GREEN
$cardCon.Line.Weight = 1.2

$tfCon = $cardCon.TextFrame
$tfCon.MarginLeft = 18
$tfCon.MarginTop = 16
$tfCon.MarginRight = 18
$tfCon.WordWrap = $msoTrue

$tfCon.TextRange.Text = "PROJECT CONCLUSION`r`n`r`n" +
    "- Production-Grade Assessment Platform:`r`n" +
    "  QuizSphere Mobile demonstrates that native Android applications with offline-first SQLite databases can deliver zero-latency evaluation while maintaining strict examination integrity.`r`n`r`n" +
    "- Elimination of Paper Inefficiencies:`r`n" +
    "  Automates grading, eliminates human calculation errors, and delivers immediate remedial diagnostics to candidates.`r`n`r`n" +
    "- Team Presentation:`r`n" +
    "  - Giri Siddharth  |  Roll No: 40013`r`n" +
    "  - Ankit Singh  |  Roll No: 40003`r`n" +
    "  - Raju Ranjan Choudhary  |  Roll No: 40043`r`n`r`n" +
    "Thank You! Questions & Discussion Welcome."

$tfCon.TextRange.Font.Name = $FONT_BODY
$tfCon.TextRange.Font.Size = 10.5
$tfCon.TextRange.Font.Color.RGB = $C_TEXT_BODY
$tfCon.TextRange.Paragraphs(1).Font.Name = $FONT_HEADING
$tfCon.TextRange.Paragraphs(1).Font.Size = 13.5
$tfCon.TextRange.Paragraphs(1).Font.Bold = $msoTrue
$tfCon.TextRange.Paragraphs(1).Font.Color.RGB = $C_GREEN

# Save Presentation
$outPptx = Join-Path $PSScriptRoot "QuizSphere_Mobile_Presentation.pptx"
Write-Host "Saving presentation to $outPptx..."
$pres.SaveAs($outPptx)
$pres.Close()
$app.Quit()
[System.Runtime.Interopservices.Marshal]::ReleaseComObject($app) | Out-Null

Write-Host "SUCCESS: Presentation generated successfully at $outPptx"

